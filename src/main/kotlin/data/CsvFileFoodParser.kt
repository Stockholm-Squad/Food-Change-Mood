package data

import model.Meal
import model.Nutrition


class CsvFileFoodParser() {

    fun parseOnLine(line: String): Meal {
        val mealInfo = splitIngredients(line)

        return Meal(
            id = mealInfo[ColumnIndex.ID].toInt(),
            name = mealInfo[ColumnIndex.NAME],
            tags = mealInfo[ColumnIndex.TAGS].split(","),
            numberOfIngredients = mealInfo[ColumnIndex.N_INGREDIENTS].toInt(),
            preparationTime = mealInfo[ColumnIndex.MINUTES].toInt(),
            steps = mealInfo[ColumnIndex.STEPS].split(","),
            addedDate = mealInfo[ColumnIndex.SUBMITTED],
            nutrition = constructNutritionInfo(mealInfo),
            description = mealInfo[ColumnIndex.DESCRIPTION],
            numberOfSteps = mealInfo[ColumnIndex.N_STEPS].toInt(),
            ingredients = mealInfo[ColumnIndex.INGREDIENTS].split(","),
            contributorId = mealInfo[ColumnIndex.CONTRIBUTOR_ID].toInt()
        )
    }

    private fun constructNutritionInfo(mealInfo: List<String>): Nutrition {
        val nutrition = splitIngredients(mealInfo.toString())
        return Nutrition(
            saturatedFat = nutrition.getFloatOrNull(ColumnIndex.SATURATED_FAT_INDEX),
            carbohydrates = nutrition.getFloatOrNull(ColumnIndex.CARBOHYDRATES),
            sugar = nutrition.getFloatOrNull(ColumnIndex.SUGAR_INDEX),
            sodium = nutrition.getFloatOrNull(ColumnIndex.SODIUM),
            calories = nutrition.getFloatOrNull(ColumnIndex.CALORIES_INDEX),
            totalFat = nutrition.getFloatOrNull(ColumnIndex.TOTAL_FAT_INDEX),
            protein = nutrition.getFloatOrNull(ColumnIndex.PROTEIN_INDEX)
        )
    }

    private fun List<String>.getFloatOrNull(index: Int): Float? {
        return this[index].toFloatOrNull()

    }

    private fun splitIngredients(ingredientsString: String): List<String> {
        return ingredientsString
            .removeSurrounding("[", "]")  // Remove the square brackets
            .split("', '")  // Split by "', '" to handle the commas between items
            .map { it.replace("'", "").trim() }  // Remove single quotes and trim spaces
    }
}