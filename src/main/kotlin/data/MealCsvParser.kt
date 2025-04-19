package data

import model.Meal
import model.Nutrition
import org.example.data.utils.CsvLineFormatter
import org.example.model.MealColumnIndex
import org.example.model.NutritionIndex
import org.example.utils.parseDate

class MealCsvParser(
    private val csvLineFormatter: CsvLineFormatter
) {

    fun parseLine(row: String): Meal {
        val mealData: List<String> = csvLineFormatter.formatRowOfData(row)
        return Meal(
            name = mealData[MealColumnIndex.NAME.index],
            id = mealData[MealColumnIndex.ID.index].toIntOrNull() ?: throw IllegalArgumentException("Missing id"),
            minutes = mealData[MealColumnIndex.MINUTES.index].toIntOrNull() ?: 0,
            contributorId = mealData[MealColumnIndex.CONTRIBUTOR_ID.index].toIntOrNull()
                ?: throw IllegalArgumentException("Missing id"),
            submitted = (mealData[MealColumnIndex.SUBMITTED.index]).parseDate(),
            tags = parseListOfData(mealData[MealColumnIndex.TAGS.index]),
            nutrition = constructNutritionFromToken(mealData[MealColumnIndex.NUTRITION.index]),
            numberOfSteps = mealData[MealColumnIndex.N_STEPS.index].toIntOrNull() ?: 0,
            steps = parseListOfData(mealData[MealColumnIndex.STEPS.index]),
            description = mealData[MealColumnIndex.DESCRIPTION.index],
            ingredients = parseListOfData(mealData[MealColumnIndex.INGREDIENTS.index]),
            numberOfIngredients = mealData[MealColumnIndex.N_INGREDIENTS.index].toIntOrNull() ?: 0
        )
    }

    private fun parseListOfData(raw: String): List<String> {
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
        val nutrition = parseListOfData(raw).map { it.trim().toFloatOrNull() }
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