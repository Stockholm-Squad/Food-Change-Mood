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
            ?: getFailureResult(Throwable("No easy meals found."))

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
        return (meal.minutes ?: 0) <= maxPrepTime &&
                (meal.numberOfIngredients ?: 0) <= maxIngredients &&
                (meal.numberOfSteps ?: 0) <= maxSteps
    }
}