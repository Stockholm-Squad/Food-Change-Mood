package org.example.logic

import model.Meal

interface MealRepository {
    fun getAllMeals():List<Meal>
}