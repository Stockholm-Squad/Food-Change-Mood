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
            nutrition = parseNutrition(mealInfo),
            numberOfSteps = mealInfo.getOrNull(ColumnIndex.N_STEPS)?.toIntOrNull(),
            steps = parseList(mealInfo.getOrNull(ColumnIndex.STEPS) ?: ""),
            description = cleanString(mealInfo.getOrNull(ColumnIndex.DESCRIPTION)),
            ingredients = parseList(mealInfo.getOrNull(ColumnIndex.INGREDIENTS) ?: ""),
            numberOfIngredients = mealInfo.getOrNull(ColumnIndex.N_INGREDIENTS)?.toIntOrNull()
        )
    }

    private fun parseNutrition(mealInfo: List<String>): Nutrition {
        val rawNutrition = mealInfo.getOrNull(ColumnIndex.NUTRITION)
            ?.trim('[', ']')
            ?.split(",")
            ?: return Nutrition()

        if (rawNutrition.size < 7) {
            return Nutrition() // Invalid format
        }
        return try {
            Nutrition(
                calories = rawNutrition[0].toFloatOrNull() ?: 0f,
                totalFat = rawNutrition[1].toFloatOrNull() ?: 0f,
                sugar = rawNutrition[2].toFloatOrNull() ?: 0f,
                sodium = rawNutrition[3].toFloatOrNull() ?: 0f,
                protein = rawNutrition[4].toFloatOrNull() ?: 0f,
                saturatedFat = rawNutrition[5].toFloatOrNull() ?: 0f,
                carbohydrates = rawNutrition[6].toFloatOrNull() ?: 0f
            )
        } catch (e: Exception) {
            println("Parse nutrition: ${e.message}")
            Nutrition() // Return default on parsing error
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