package org.example.data

import model.Meal
import org.example.data.source.CsvMealDataSource
import org.example.logic.repo.IMealRepository

class CsvMealsRepositoryImpl(
       private val csvMealDataSource: CsvMealDataSource
) : IMealRepository {
    override fun getAllMeals(): List<Meal> =csvMealDataSource.loadMeals()

}
