package org.example.data.dataSource

import model.Meal
import org.example.Results.ReaderResult

interface MealDataSource {
    fun getAllMeals(): ReaderResult<List<Meal>>
}