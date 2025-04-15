package org.example.logic

import model.Meal

interface IMealsRepository {
    fun getAllMeals():List<Meal>
}