package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetSeaFoodByProteinRankUseCase(private val mealsRepository: MealsRepository) {
    fun getSeaFoodByProteinRank(): Result<List<Meal>> {
        return mealsRepository.getAllMeals().fold(
            onSuccess = { meals -> getSuccessResult(meals) },
            onFailure = { failure -> getFailureResult(failure) }
        )
    }

    private fun getSuccessResult(meals: List<Meal>): Result<List<Meal>> {
        return meals.filter { meal ->
            meal.description?.contains(
                "seafood",
                ignoreCase = true
            ) == true
        }.sortedByDescending { meal -> meal.nutrition?.protein }.takeIf { meal -> meal.isNotEmpty() }
            ?.let { Result.success(it) } ?: Result.failure(NoSuchElementException("No seafood meals found."))
    }
    private fun getFailureResult(throwable: Throwable): Result<List<Meal>>{
        return  Result.failure(exception = throwable)
    }
}