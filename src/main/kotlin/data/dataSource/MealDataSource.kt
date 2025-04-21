package org.example.data.dataSource

import data.ReaderResult
import model.Meal

interface MealDataSource {
    fun getAllMeals(): ReaderResult<List<Meal>>
}