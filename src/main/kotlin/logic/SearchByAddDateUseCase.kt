package org.example.logic

import model.Meal
import org.example.utils.DateValidator

class SearchByAddDateUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getMealsByDate(date: String): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter { meal ->
                hasDate(meal = meal, date = date)
            }.sortedBy { it.id }
    }

    private fun hasDate(meal: Meal, date: String): Boolean {
        val utilDate = DateValidator.getDateFromString(date)
        return meal.submitted == utilDate
    }
}