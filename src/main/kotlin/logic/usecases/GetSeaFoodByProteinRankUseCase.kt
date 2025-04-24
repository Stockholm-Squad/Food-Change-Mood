package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.model.FoodChangeMoodExceptions

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
            ) == true &&
            meal.nutrition?.protein != null
        }.sortedWith(
            compareByDescending<Meal> { it.nutrition!!.protein }
                .thenBy { it.name }
        ).takeIf { meals -> meals.isNotEmpty() }
            ?.let { Result.success(it) } ?: Result.failure(FoodChangeMoodExceptions.LogicException.NoSeaFoodMealsFound())
    }
    private fun getFailureResult(throwable: Throwable): Result<List<Meal>>{
        return  Result.failure(exception = throwable)
    }

}