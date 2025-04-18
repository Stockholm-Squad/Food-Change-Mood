package org.example.logic

import model.Meal

class GetGuessGameUseCase(
    private val mealsRepository: MealsRepository
) {
    //TODO handle the game here instead of handling it throw UI
    fun getRandomMeal(): Meal {
        val meals=mealsRepository.getAllMeals()
        val randomMeal=meals.random()
        return randomMeal
    }
}