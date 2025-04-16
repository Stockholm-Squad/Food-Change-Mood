package org.example.logic.repo

import model.Meal

interface IMealsRepository {
    fun getAllMeals():List<Meal>
}