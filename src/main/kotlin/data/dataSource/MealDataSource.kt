package org.example.data.dataSource

import model.Meal
import org.example.model.Result

interface MealDataSource {
    fun getAllMeals(): Result<List<Meal>>
}