package org.example.logic

import model.Meal

class GetGuessGameUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getRandomMeal(): Meal {
        val meals=mealsRepository.getAllMeals()
        val randomMeal=meals.random()
        return randomMeal
    }
}