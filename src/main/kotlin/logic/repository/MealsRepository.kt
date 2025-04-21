package org.example.logic.repository

import model.Meal
import org.example.logic.model.Results


interface MealsRepository {
    fun getAllMeals(): Results<List<Meal>>
}