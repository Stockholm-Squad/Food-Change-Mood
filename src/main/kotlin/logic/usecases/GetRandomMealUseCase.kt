package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.Constants


class GetRandomMealUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getRandomMeal(): Result<Meal> {
        return mealsRepository.getAllMeals().fold(
            onSuccess = { meals ->
                if (meals.isNotEmpty()) {
                    Result.success(meals.random())
                } else {
                    Result.failure(Throwable(Constants.NO_MEALS_FOUND))
                }
            },
            onFailure = { error ->
                Result.failure(Throwable("${Constants.ERROR_FETCHING_MEALS} ${error.message}"))
            }
        )
    }

}