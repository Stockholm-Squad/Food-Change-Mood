package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.DateValidator

//TODO rename this use case to be ex: GetMealsByDate
class SearchByAddDateUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getMealsByDate(date: String): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter { meal ->
                hasDate(meal = meal, date = date)
            }.sortedBy { it.id }
    }
    //TODO rename this to be more readable
    private fun hasDate(meal: Meal, date: String): Boolean {
        val utilDate = DateValidator.getDateFromString(date)
        return meal.submitted == utilDate
    }
}