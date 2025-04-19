package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.DateValidator

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
        val utilDate = DateValidator.getDateFromString(date)
        return meal.submitted == utilDate
    }
}