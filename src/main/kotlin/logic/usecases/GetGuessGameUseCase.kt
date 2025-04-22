package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository


class GetGuessGameUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getRandomMeal(): Result<Meal> {
        return mealsRepository.getAllMeals().fold(
            onSuccess = { meals ->
                Result.success(
                    meals.random()
                )
            },
            onFailure = { error ->
                Result.failure(error)

            }
        )
    }
}