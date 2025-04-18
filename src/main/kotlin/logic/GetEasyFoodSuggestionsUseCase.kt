package org.example.logic

import model.Meal

class GetEasyFoodSuggestionsUseCase(
    private val mealRepository: MealsRepository
) {

    fun getEasyFood(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter { it.isEasyMeal() }
            .shuffled()
            .take(10)
            .ifEmpty { throw NoSuchElementException("No easy meals found.") }
    }

    // Extension function for Meal class to test isEasyMeal
    fun Meal.isEasyMeal(
        maxPrepTime: Int = 30,
        maxIngredients: Int = 5,
        maxSteps: Int = 6
    ): Boolean {
        return (minutes ?: 0) <= maxPrepTime &&
                (numberOfIngredients ?: 0) <= maxIngredients &&
                (numberOfSteps ?: 0) <= maxSteps
    }
}