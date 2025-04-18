package data

import model.Meal
import model.Nutrition
import org.example.data.NutritionIndex
import org.example.utils.parseDate

class MealCsvParser {

    fun parseLine(row: String): Meal {
        val listOfLines: List<String> = formatLineOfData(row)
        return Meal(
            name = listOfLines[MealColumnIndex.NAME.index],
            id = listOfLines[MealColumnIndex.ID.index].toIntOrNull() ?: throw IllegalArgumentException("Missing id"),
            minutes = listOfLines[MealColumnIndex.MINUTES.index].toIntOrNull() ?: 0,
            contributorId = listOfLines[MealColumnIndex.CONTRIBUTOR_ID.index].toIntOrNull()
                ?: throw IllegalArgumentException("Missing id"),
            submitted = (listOfLines[MealColumnIndex.SUBMITTED.index]).parseDate(),
            tags = parseListOfData(listOfLines[MealColumnIndex.TAGS.index]),
            nutrition = constructNutritionFromToken(listOfLines[MealColumnIndex.NUTRITION.index]),
            numberOfSteps = listOfLines[MealColumnIndex.N_STEPS.index].toIntOrNull() ?: 0,
            steps = parseListOfData(listOfLines[MealColumnIndex.STEPS.index]),
            description = listOfLines[MealColumnIndex.DESCRIPTION.index],
            ingredients = parseListOfData(listOfLines[MealColumnIndex.INGREDIENTS.index]),
            numberOfIngredients = listOfLines[MealColumnIndex.N_INGREDIENTS.index].toIntOrNull() ?: 0
        )
    }

    private fun formatLineOfData(str: String): List<String> {
        val result = mutableListOf<String>()
        val StrBuilder = StringBuilder()
        var insideQuotes = false
        for (char in str) {
            when (char) {
                '"' -> {
                    insideQuotes = !insideQuotes
                    StrBuilder.append(char)
                }

                ',' -> {
                    if (insideQuotes) StrBuilder.append(char)
                    else {
                        result.add(StrBuilder.toString())
                        StrBuilder.clear()
                    }
                }

                else -> StrBuilder.append(char)
            }
        }
        if (StrBuilder.isNotEmpty()) result.add(StrBuilder.toString())
        return result
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