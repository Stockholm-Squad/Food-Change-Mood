package data

import model.Meal
import model.Nutrition

class FoodCsvParser {

    fun parseOneLine(mealInfo: List<String>): Meal {
        return Meal(
            name = cleanString(mealInfo.getOrNull(ColumnIndex.NAME)),
            id = mealInfo.getOrNull(ColumnIndex.ID)?.toIntOrNull() ?: 0,
            minutes = mealInfo.getOrNull(ColumnIndex.MINUTES)?.toIntOrNull(),
            contributorId = mealInfo.getOrNull(ColumnIndex.CONTRIBUTOR_ID)?.toIntOrNull() ?: 0,
            submitted = mealInfo.getOrNull(ColumnIndex.SUBMITTED),
            tags = parseList(mealInfo.getOrNull(ColumnIndex.TAGS) ?: ""),
            nutrition = parseNutrition(mealInfo.getOrNull(ColumnIndex.NUTRITION)),
            numberOfSteps = mealInfo.getOrNull(ColumnIndex.N_STEPS)?.toIntOrNull(),
            steps = parseList(mealInfo.getOrNull(ColumnIndex.STEPS) ?: ""),
            description = cleanString(mealInfo.getOrNull(ColumnIndex.DESCRIPTION)),
            ingredients = parseList(mealInfo.getOrNull(ColumnIndex.INGREDIENTS) ?: ""),
            numberOfIngredients = mealInfo.getOrNull(ColumnIndex.N_INGREDIENTS)?.toIntOrNull()
        )
    }

    private fun parseNutrition(rawNutrition: String?): Nutrition {
        val defaultNutrition = Nutrition()
        val nutritionValues = rawNutrition
            ?.removeSurrounding("[", "]") // Safely remove brackets
            ?.split(",")
            ?.map { it.trim().trim('"', '\'') } // Remove leftover quotes
            ?: return defaultNutrition

        return if (nutritionValues.size == 7) {
            Nutrition(
                calories = nutritionValues[0].toFloatOrNull() ?: 0f,
                totalFat = nutritionValues[1].toFloatOrNull() ?: 0f,
                sugar = nutritionValues[2].toFloatOrNull() ?: 0f,
                sodium = nutritionValues[3].toFloatOrNull() ?: 0f,
                protein = nutritionValues[4].toFloatOrNull() ?: 0f,
                saturatedFat = nutritionValues[5].toFloatOrNull() ?: 0f,
                carbohydrates = nutritionValues[6].toFloatOrNull() ?: 0f
            )
        } else {
            println("Malformed nutrition data: $rawNutrition")
            defaultNutrition
        }
    }

    private fun parseList(input: String): List<String> {
        if (input.isBlank()) return emptyList()

        return input.trim()
            .replace(Regex("[\\[\\]]"), "")  // Remove ALL brackets
            .trim('\'', '"')  // Remove surrounding quotes
            .split(",")
            .map { it.trim().trim('\'', '"') }
            .filter { it.isNotBlank() }
    }

    private fun cleanString(input: String?): String {
        return input?.trim()
            ?.takeIf { it.isNotBlank() } ?: ""
    }


}