package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetIraqiMealsUseCase(
    private val mealRepository: MealsRepository,
) {
    fun getIraqiMales(): List<Meal> {
        return mealRepository.getAllMeals()
            .takeIf { meals ->
                meals.isNotEmpty()
            }
            ?.filter { meal ->
                meal.description?.contains("iraq", ignoreCase = true) == true || meal.tags?.contains("iraqi") == true
            } ?: throw IllegalStateException("Sorry, we don't have any iraqi meals available")

    }


}