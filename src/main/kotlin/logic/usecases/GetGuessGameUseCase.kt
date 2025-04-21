package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository


class GetGuessGameUseCase(
    private val mealsRepository: MealsRepository
) {


    fun getRandomMeal(): Results<Meal> {
        return when (val meals = mealsRepository.getAllMeals()) {
            is Results.Success -> {
                Results.Success(meals.model.random())
            }
            is Results.Fail -> Results.Fail(meals.exception)
        }
    }
}