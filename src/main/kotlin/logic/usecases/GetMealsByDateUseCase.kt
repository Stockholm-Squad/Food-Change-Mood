package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.getDateFromString

class GetMealsByDateUseCase(
    private val mealsRepository: MealsRepository
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
        val localDate = getDateFromString(date).fold(
            onSuccess = { dateResult -> dateResult },
            onFailure = { exception ->
                println(exception)
                false
            }
        )
        return meal.submitted == localDate
    }
}