package org.example.data.dataSource

import model.Meal
import org.example.logic.model.Results

interface MealDataSource {
    fun getAllMeals(): Results<List<Meal>>
}