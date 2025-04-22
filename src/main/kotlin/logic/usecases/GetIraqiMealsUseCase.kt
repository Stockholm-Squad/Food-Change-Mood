package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetIraqiMealsUseCase(
    private val mealRepository: MealsRepository,
) {
    fun getIraqiMeals(): Result<List<Meal>> {
        return mealRepository.getAllMeals().fold(
           onSuccess = { meals -> Result.success(getSuccessResult(meals))},
            onFailure = ::getFailureResult
        )
    }

    private fun getSuccessResult(meals: List<Meal>): List<Meal> {
        return meals.filter { meal ->
                    meal.description?.contains("iraq", ignoreCase = true) == true
                            || meal.tags?.contains("iraqi") == true}
        }

    private fun getFailureResult(throwable: Throwable): Result<List<Meal>>{
        return  Result.failure(exception = throwable)
    }
}



