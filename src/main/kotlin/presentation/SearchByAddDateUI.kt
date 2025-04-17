package presentation;

import model.Meal
import org.example.logic.MealsRepository
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date

/*
8- Search Foods by Add Date: Use Kotlin’s Date class to represent the date in the meal entity.
    Let the user input a date and return a list of IDs and names of meals added on that date.
    The user should be able to view details of a specific meal by entering its ID. Handle exceptions for:
        - Incorrect date format.
        - No meals were found for the given date. Ensure different exceptions are used for both cases.
 */

class SearchByAddDateUI(
    private val mealsRepository: MealsRepository
) {

    private fun isValidDate(date: String): Boolean {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE // Default format: yyyy-MM-dd
        return try {
            LocalDate.parse(date, formatter) // Try to parse the date
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun searchMealsByDate() {
        while (true) {
            println("📅 Enter date (YYYY-MM-DD):\n or 0 to exit")
            val date = readlnOrNull()

            if (date != null && date == "0") {
                return
            } else if (date != null && isValidDate(date)) {
                searchFood(date)
            } else {
                println("Enter a valid Date or zero => 0")
            }
        }
    }

    private fun searchFood(date: String) {
        val filteredList = getMealsByDate(date)
        filteredList.forEach { meal ->
            println("${meal.id} -> ${meal.name}")
        }
        while (true) {
            println()
            println("-1 -> search again")
            println("meal id -> view details")
            val input = readlnOrNull()
            val mealId = input?.toIntOrNull()

            if (mealId == null) {
                println("Enter a valid ID or -1")
                continue
            } else if (mealId == -1) {
                break
            } else {
                viewMealDetails(filteredList, mealId)
            }
        }
    }

    private fun viewMealDetails(filteredList: List<Meal>, mealId: Int) {
        val meal: Meal? = filteredList.find { meal ->
            meal.id == mealId
        }

        if (meal == null) {
            println("The meal with ID $mealId does not exist.")
            return
        }

        println(meal)
    }

    private fun getMealsByDate(date: String): List<Meal> {
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