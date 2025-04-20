package org.example.data.repository

import model.Meal
import org.example.data.dataSource.MealDataSource
import org.example.logic.model.FoodChangeModeResults
import org.example.logic.repository.MealsRepository


class MealCsvRepository(
    private val mealDatasource: MealDataSource?
) : MealsRepository {


    override fun getAllMeals(): FoodChangeModeResults<List<Meal>> {
        return allMeals
            .takeIf { it.isNotEmpty() }
            ?.let { FoodChangeModeResults.Success(it) }
            ?: runCatching {
                mealDatasource?.getAllMeals() ?: emptyList()
            }.onSuccess {
                allMeals = it
            }.fold(
                onSuccess = { FoodChangeModeResults.Success(it) },
                onFailure = { FoodChangeModeResults.Fail(it) }
            )
    }

    companion object {
        private var allMeals: List<Meal> = emptyList()
    }
}