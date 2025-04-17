package org.example.logic

import model.Meal
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class SearchByAddDateUseCase(
    private val mealsRepository: MealsRepository
) {

    fun isValidDate(date: String): Boolean {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE // Default format: yyyy-MM-dd
        return try {
            LocalDate.parse(date, formatter) // Try to parse the date
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun getMealsByDate(date: String): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter { meal ->
                hasDate(meal = meal, date = date)
            }.sortedBy { it.id }
    }


    private fun hasDate(meal: Meal, date: String): Boolean {
        val localDate = LocalDate.parse(date)
        val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val utilDate: Date = Date.from(instant)
        return meal.submitted == utilDate
    }
}