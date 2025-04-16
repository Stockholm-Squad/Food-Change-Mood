package org.example.data

import model.Meal
import org.example.data.source.CsvMealDataSource
import org.example.logic.repo.IMealsRepository

class CsvMealsRepositoryImpl(
       private val csvMealDataSource: CsvMealDataSource
) : IMealsRepository {
    override fun getAllMeals(): List<Meal> =csvMealDataSource.loadMeals()

}
