package org.example.data.repository

import model.Meal
import org.example.data.dataSource.MealDataSource
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository


class MealCsvRepository(
    private val mealDatasource: MealDataSource
) : MealsRepository {


    override fun getAllMeals(): Results<List<Meal>> {
        return allMeals
            .takeIf { it.isNotEmpty() }?.let { Results.Success(it) }
            ?: handleFailure(Throwable("Error while loading data")).apply {
                when (val result = mealDatasource.getAllMeals()) {
                    is Results.Success -> handleSuccess(result.model)
                    is Results.Fail -> handleFailure(result.exception)
                }
            }
    }

    companion object {
        private var allMeals: List<Meal> = emptyList()
    }

    private fun handleSuccess(meals: List<Meal>): Results<List<Meal>> {
        allMeals = meals
        return Results.Success(allMeals)

    }

    private fun handleFailure(throwable: Throwable): Results<List<Meal>> {
        return Results.Fail(throwable)
    }
}