package org.example.data.repository

import model.Meal
import org.example.data.dataSource.MealDataSource
import org.example.logic.repository.MealsRepository


class MealCsvRepository(
    private val mealDatasource: MealDataSource?
) : MealsRepository {

    override fun getAllMeals(): List<Meal> {
        if (allMeals.isNotEmpty()) return allMeals

        allMeals = mealDatasource?.getAllMeals() ?: emptyList()
        return allMeals
    }

    companion object {
        private var allMeals: List<Meal> = emptyList()
    }
}