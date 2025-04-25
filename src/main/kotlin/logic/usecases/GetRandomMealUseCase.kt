package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.model.FoodChangeMoodExceptions
import org.example.utils.Constants


class GetRandomMealUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getRandomMeal(): Result<Meal> {
        return mealsRepository.getAllMeals().fold(
            onSuccess = { meals ->
                Result.success(randomMeal(meals))
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    private fun randomMeal(meals: List<Meal>): Meal {
        return meals.takeIf { it.isNotEmpty() }
            ?.random() ?: throw FoodChangeMoodExceptions.LogicException.NoMealsFound()
    }
}