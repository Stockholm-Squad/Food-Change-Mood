package logic.usecases.utils

import model.Meal
import model.Nutrition

class MealCreationMocker {
    fun getGymHelperMeal(name: String, calories: Float, proteins: Float): Meal {
        return Meal(
            name = name,
            id = 1,
            minutes = 30,
            contributorId = 2,
            submitted = null,
            tags = null,
            nutrition = Nutrition(
                calories = calories,
                totalFat = 40F,
                sugar = 40F,
                sodium = 40F,
                protein = proteins,
                saturatedFat = 40F,
                carbohydrates = 40F
            ),
            numberOfSteps = 4,
            steps = null,
            description = "yuui",
            ingredients = null,
            numberOfIngredients = 5
        )
    }
}