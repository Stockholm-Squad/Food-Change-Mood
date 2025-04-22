package org.example.logic.repository

import model.Meal


interface MealsRepository {
    fun getAllMeals(): Result<List<Meal>>
}