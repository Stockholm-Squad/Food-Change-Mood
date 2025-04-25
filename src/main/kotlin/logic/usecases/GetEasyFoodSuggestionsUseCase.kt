package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetEasyFoodSuggestionsUseCase(
    private val mealRepository: MealsRepository
) {

    fun getEasyFood(): Result<List<Meal>> {
        return mealRepository.getAllMeals()
            .fold(
                onSuccess = ::getSuccessResult,
                onFailure = ::getFailureResult
            )

    }

    private fun getSuccessResult(allMeals: List<Meal>): Result<List<Meal>> {
        return allMeals.filter {
            isEasyMeal(it)
        }.shuffled()
            .takeIf { it.isNotEmpty() }
            ?.take(10)
            ?.let { Result.success(it) }
            ?: getFailureResult(NoSuchElementException("No easy meals found."))

    }

    private fun getFailureResult(throwable: Throwable): Result<List<Meal>> {
        return Result.failure(throwable)
    }

    private fun isEasyMeal(
        meal: Meal,
        maxPrepTime: Int = 30,
        maxIngredients: Int = 5,
        maxSteps: Int = 6
    ): Boolean {
        val minutes = meal.minutes
        val ingredients = meal.numberOfIngredients
        val steps = meal.numberOfSteps

        // Only evaluate meals where all fields are non-null
        return minutes != null && ingredients != null && steps != null &&
                minutes <= maxPrepTime &&
                ingredients <= maxIngredients &&
                steps <= maxSteps
    }

}