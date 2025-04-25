package org.example.data.parser

import kotlinx.datetime.LocalDate
import model.Meal
import model.Nutrition
import org.example.data.utils.LineFormater
import org.example.model.FoodChangeMoodExceptions.ValidationException
import org.example.utils.Constants
import org.example.utils.DateParser
import org.example.utils.MealColumnIndex
import org.example.utils.NutritionIndex

class MealCsvParser(
    private val csvLineFormatter: LineFormater,
    private val dateParser: DateParser
) : MealParser {

    override fun parseLine(row: String): Result<Meal> {
        return try {
            val mealRow = csvLineFormatter.formatMealLine(row)
            validateMealRow(mealRow = mealRow)
            val meal = Meal(
                name = extractStringColumn(mealRow, MealColumnIndex.NAME),
                id = extractIntColumn(mealRow, MealColumnIndex.ID, "Missing ID"),
                minutes = extractIntColumn(mealRow, MealColumnIndex.MINUTES, defaultValue = 0),
                contributorId = extractIntColumn(mealRow, MealColumnIndex.CONTRIBUTOR_ID, "Missing Contributor ID"),
                submitted = extractDateColumn(mealRow, MealColumnIndex.SUBMITTED),
                tags = parseListOfString(extractStringColumn(mealRow, MealColumnIndex.TAGS)),
                nutrition = constructNutritionFromToken(extractStringColumn(mealRow, MealColumnIndex.NUTRITION)),
                numberOfSteps = extractIntColumn(mealRow, MealColumnIndex.N_STEPS, defaultValue = 0),
                steps = parseListOfString(extractStringColumn(mealRow, MealColumnIndex.STEPS)),
                description = extractStringColumn(mealRow, MealColumnIndex.DESCRIPTION),
                ingredients = parseListOfString(extractStringColumn(mealRow, MealColumnIndex.INGREDIENTS)),
                numberOfIngredients = extractIntColumn(mealRow, MealColumnIndex.N_INGREDIENTS, defaultValue = 0)
            )
            Result.success(meal)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validateMealRow(mealRow: List<String>): Result<Unit> {
        if (mealRow.isEmpty() || mealRow.size < MealColumnIndex.entries.size) {
            return Result.failure(ValidationException.RowParsingException(Constants.INSUFFICIANT_DATA_IN_ROW))
        }
        return Result.success(Unit)
    }

    private fun extractStringColumn(
        mealRow: List<String>,
        index: MealColumnIndex
    ): String {
        return safeAccessColumn(mealRow, index.index, "String") { it.trim() }
    }

    private fun extractIntColumn(
        mealRow: List<String>,
        index: MealColumnIndex,
        errorMessage: String? = null,
        defaultValue: Int? = null
    ): Int {
        return safeAccessColumn(mealRow, index.index, "Integer") { it.toIntOrNull() }
            ?: run {
                if (errorMessage != null) throw IllegalArgumentException(errorMessage)
                defaultValue ?: throw IllegalArgumentException("Invalid integer value at index ${index.index}")
            }
    }

    private fun extractDateColumn(
        mealRow: List<String>,
        index: MealColumnIndex
    ): LocalDate? {
        return safeAccessColumn(mealRow, index.index, "Date") { dateField ->
            dateParser.getDateFromString(dateField).fold(
                onSuccess = { date -> date },
                //TODO we need to handle the Result more not just return null
                onFailure = { exception ->
                    println("Invalid date format at index ${index.index}: $dateField" + exception)
                    null
                }
            )
        }
    }

    private fun <T> safeAccessColumn(
        mealRow: List<String>,
        columnIndex: Int,
        columnType: String,
        transform: (String) -> T
    ): T {
        return if (columnIndex in mealRow.indices) {
            transform(mealRow[columnIndex])
        } else {
            throw IndexOutOfBoundsException("Column '${MealColumnIndex.entries.find { it.index == columnIndex }?.name}' ($columnType) is out of bounds in row: $mealRow")
        }
    }

    private fun parseListOfString(raw: String): List<String> {
        return raw
            .split(",")
            .map {
                it.replace("'", "")
                    .replace("\"", "")
                    .replace("[", "")
                    .replace("]", "").trim()
            }
            .filter { it.isNotBlank() }
    }

    private fun constructNutritionFromToken(raw: String): Nutrition {
        val nutrition = parseListOfString(raw).map { it.trim().toFloatOrNull() }
        return Nutrition(
            calories = nutrition[NutritionIndex.CALORIES.index] ?: 0.0F,
            totalFat = nutrition[NutritionIndex.TOTAL_FAT.index] ?: 0.0F,
            sugar = nutrition[NutritionIndex.SUGAR.index] ?: 0.0F,
            sodium = nutrition[NutritionIndex.SODIUM.index] ?: 0.0F,
            protein = nutrition[NutritionIndex.PROTEIN.index] ?: 0.0F,
            saturatedFat = nutrition[NutritionIndex.SATURATED_FAT.index] ?: 0.0F,
            carbohydrates = nutrition[NutritionIndex.CARBOHYDRATES.index] ?: 0.0F,
        )
    }
}

