package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.model.FoodChangeMoodExceptions.LogicException.NoIraqiMeals

class GetIraqiMealsUseCase(
    private val mealRepository: MealsRepository,
) {
    fun getIraqiMeals(): Result<List<Meal>> {
        return mealRepository.getAllMeals().fold(
            onSuccess = ::getSuccessResult,
            onFailure = ::getFailureResult
        )
    }

    private fun getSuccessResult(meals: List<Meal>): Result<List<Meal>> {
        return Result.success(
            meals.filter { meal ->
                meal.tags?.any { tags -> tags.contains("iraqi", true) } == true ||
                        meal.description?.contains("iraq", ignoreCase = true) == true
            }.takeIf { iraqiMeals -> iraqiMeals.isNotEmpty() } ?: return Result.failure(NoIraqiMeals())
        )
    }

    private fun getFailureResult(throwable: Throwable): Result<List<Meal>> {
        return Result.failure(NoIraqiMeals())
    }

}