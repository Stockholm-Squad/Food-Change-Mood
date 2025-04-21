package org.example.data.repository

import model.Meal
import org.example.Results.ReaderResult
import org.example.data.dataSource.MealDataSource
import org.example.logic.repository.MealsRepository


class MealCsvRepository(
    private val mealDatasource: MealDataSource?
) : MealsRepository {

    //TODO change the return type to be from result instead of List<Meal> direct
    //TODO update all usage of this function to receive Result instead of List<Meal> direct
    override fun getAllMeals(): List<Meal> {
        if (allMeals.isNotEmpty()) return allMeals
        //TODO make try & catch exception and return fail or success based on the result from getAllMeals
//        allMeals = mealDatasource?.getAllMeals() ?: emptyList()

        allMeals = when (val result = mealDatasource?.getAllMeals()) {
            is ReaderResult.Success -> {
                result.value
            }
            is ReaderResult.Failure -> {
                println("Failed to fetch meals: ${result.errorMessage}")
                result.cause?.printStackTrace()
                emptyList()
            }

            null -> emptyList()
        }

        return allMeals
    }

    companion object {
        private var allMeals: List<Meal> = emptyList()
    }
}