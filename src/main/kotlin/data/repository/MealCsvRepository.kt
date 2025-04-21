package org.example.data.repository

import model.Meal
import org.example.model.Result
import org.example.data.dataSource.MealDataSource
import org.example.logic.repository.MealsRepository


class MealCsvRepository(
    private val mealDatasource: MealDataSource?
) : MealsRepository {

    //TODO change the return type to be from result instead of List<Meal> direct
    //TODO update all usage of this function to receive Result instead of List<Meal> direct
    override fun getAllMeals(): List<Meal> {
        if (allMeals.isNotEmpty()) return allMeals

        allMeals = mealDatasource?.getAllMeals() ?: emptyList()

        return allMeals
    }

    companion object {
        private var allMeals: List<Meal> = emptyList()
    }
}