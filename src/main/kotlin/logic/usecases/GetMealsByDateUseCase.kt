package org.example.logic.usecases

import model.Meal
import org.example.logic.model.Results
import org.example.logic.repository.MealsRepository
import org.example.utils.getDateFromString

class GetMealsByDateUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getMealsByDate(date: String): Results<List<Meal>> {
        return when (val allMeals = mealsRepository.getAllMeals()) {

            is Results.Success -> Results.Success(
                allMeals.model
                    .filter { meal ->
                        isMealWithDate(meal = meal, date = date)
                    }.sortedBy { it.id })

            is Results.Fail -> Results.Fail(allMeals.exception)
        }
    }

    private fun isMealWithDate(meal: Meal, date: String): Boolean {
        val utilDate = when (val dateResult = getDateFromString(date)) {
            is Results.Fail -> {
                println(dateResult.exception)
                return false
            }

            is Results.Success -> dateResult.model
        }
        return meal.submitted == utilDate
    }
}