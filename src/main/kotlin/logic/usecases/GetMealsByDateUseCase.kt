package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.parseDate

class GetMealsByDateUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getMealsByDate(date: String): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter { meal ->
                isMealWithDate(meal = meal, date = date)
            }.sortedBy { it.id }
    }

    private fun isMealWithDate(meal: Meal, date: String): Boolean {
        val utilDate = date.parseDate()
        return meal.submitted == utilDate
    }
}