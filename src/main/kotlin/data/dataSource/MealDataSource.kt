package org.example.data.dataSource

import model.Meal

interface MealDataSource {
    fun getAllMeals(): Result<List<Meal>>
}