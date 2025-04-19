package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

class GetDessertsWithNoEggs(private val mealRepository: MealsRepository) {

    fun getDessertsWithNoEggs(): List<Meal> {
        return try {
            mealRepository.getAllMeals()
                .filter { !it.ingredients.toString().contains("egg") && it.tags.toString().contains("dessert") }
        } catch (e: Exception) {
            emptyList<Meal>()
        }
    }
}

