package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.DateParser

class GetMealsByDateUseCase(
    private val mealsRepository: MealsRepository,
    private val dateParser: DateParser,

    ) {

    fun getMealsByDate(date: String): Result<List<Meal>> {
        return mealsRepository.getAllMeals().fold(
            onSuccess = { allMeals ->
                Result.success(filterMealsByDate(allMeals, date))
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    private fun filterMealsByDate(meals: List<Meal>, date: String): List<Meal> {
        return meals
            .filter { meal ->
                isMealWithDate(meal = meal, date = date)
            }.sortedBy { it.id }
    }

    private fun isMealWithDate(meal: Meal, date: String): Boolean {
        return dateParser.getDateFromString(date).fold(
            onSuccess = { dateResult ->
                meal.submitted == dateResult
            },
            onFailure = { exception ->
                println(exception)
                false
            }
        )
    }
}