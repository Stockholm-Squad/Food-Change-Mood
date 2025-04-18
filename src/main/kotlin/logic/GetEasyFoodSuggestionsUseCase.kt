package org.example.logic

import model.Meal
import java.util.Collections.emptyList

class GetEasyFoodSuggestionsUseCase(
    private val mealRepository: MealsRepository
) {
    //TODO refactor it to take no parameter
    fun getEasyFood(
        filterEasyMeal: (Meal) -> Boolean = { it.isEasyMeal() }
    ): List<Meal> {
        return mealRepository.getAllMeals()
            .filter(filterEasyMeal)
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