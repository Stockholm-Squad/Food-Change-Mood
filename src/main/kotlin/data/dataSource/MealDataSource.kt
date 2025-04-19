package org.example.data.dataSource

import model.Meal

interface MealDataSource {
    fun getAllMeals(): List<Meal>
}