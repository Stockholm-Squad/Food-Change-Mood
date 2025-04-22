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
                Result.success(allMeals
                    .filter { meal ->
                        isMealWithDate(meal = meal, date = date)
                    }.sortedBy { it.id })
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

    private fun isMealWithDate(meal: Meal, date: String): Boolean {
        val utilDate = getDateFromString(date).fold(
            onSuccess = { dateResult -> dateResult },
            onFailure = {exception ->
                println(exception)
                false
            }
        )
        return meal.submitted == utilDate
    }
}