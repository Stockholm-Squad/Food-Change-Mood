package data

import model.Meal
import model.Nutrition
import org.example.utils.parseDate

class FoodCsvParser {

    fun parseLine(row: String): Meal {
        val listOfLines: List<String> = formatLineOfData(row)
        return Meal(
            name = listOfLines[ColumnIndex.NAME],
            id = listOfLines[ColumnIndex.ID].toIntOrNull() ?: throw IllegalArgumentException("Missing id"),
            minutes = listOfLines[ColumnIndex.MINUTES].toIntOrNull() ?: 0,
            contributorId = listOfLines[ColumnIndex.CONTRIBUTOR_ID].toIntOrNull()
                ?: throw IllegalArgumentException("Missing id"),
            submitted = (listOfLines[ColumnIndex.SUBMITTED]).parseDate(),
            tags = parseListOfData(listOfLines[ColumnIndex.TAGS]),
            nutrition = constructNutritionFromToken(listOfLines[ColumnIndex.NUTRITION]),
            numberOfSteps = listOfLines[ColumnIndex.N_STEPS].toIntOrNull() ?: 0,
            steps = parseListOfData(listOfLines[ColumnIndex.STEPS]),
            description = listOfLines[ColumnIndex.DESCRIPTION],
            ingredients = parseListOfData(listOfLines[ColumnIndex.INGREDIENTS]),
            numberOfIngredients = listOfLines[ColumnIndex.N_INGREDIENTS].toIntOrNull() ?: 0
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
            calories = nutrition[NutritionIndex.CALORIES_INDEX] ?: 0.0F,
            totalFat = nutrition[NutritionIndex.TOTAL_FAT_INDEX] ?: 0.0F,
            sugar = nutrition[NutritionIndex.SUGAR_INDEX] ?: 0.0F,
            sodium = nutrition[NutritionIndex.SODIUM] ?: 0.0F,
            protein = nutrition[NutritionIndex.PROTEIN_INDEX] ?: 0.0F,
            saturatedFat = nutrition[NutritionIndex.SATURATED_FAT_INDEX] ?: 0.0F,
            carbohydrates = nutrition[NutritionIndex.CARBOHYDRATES] ?: 0.0F,
        )
    }


}