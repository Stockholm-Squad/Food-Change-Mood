package org.example.logic.repository

import model.Meal
import org.example.logic.model.FoodChangeModeResults


interface MealsRepository {
    fun getAllMeals(): FoodChangeModeResults<List<Meal>>
}