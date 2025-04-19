package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

//TODO handle name of the useCase
class GetDessertsWithNoEggs(private val mealRepository: MealsRepository) {

    //TODO handle it to return direct instead of init variables
    fun getDessertsWithNoEggs(): List<Meal> {
        val meals = mealRepository.getAllMeals()
        return try {
            meals.filter { !it.ingredients.toString().contains("egg") && it.tags.toString().contains("dessert") }
        } catch (e: Exception) {
            emptyList<Meal>()
        }
    }
}

