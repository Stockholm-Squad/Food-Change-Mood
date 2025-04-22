package org.example.data.repository

import model.Meal
import org.example.data.dataSource.MealDataSource
import org.example.logic.repository.MealsRepository


class MealCsvRepository(
    private val mealDatasource: MealDataSource
) : MealsRepository {


    override fun getAllMeals(): Result<List<Meal>> {
        return allMeals
            .takeIf { it.isNotEmpty() }?.let { Result.success(it) }
            ?: mealDatasource.getAllMeals().fold(
                onSuccess = { meals ->
                    handleSuccess(meals)
                },
                onFailure = { exception -> handleFailure(exception) }
            )

    }

    companion object {
        private var allMeals: List<Meal> = emptyList()
    }

    private fun handleSuccess(meals: List<Meal>): Result<List<Meal>> {
        allMeals = meals
        return Result.success(allMeals)

    }

    private fun handleFailure(throwable: Throwable): Result<List<Meal>> {
        return Result.failure(throwable)
    }
}