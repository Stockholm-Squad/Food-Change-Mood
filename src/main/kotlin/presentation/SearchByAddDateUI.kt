package presentation;

import data.FoodCsvParser
import data.FoodCsvReader
import model.Meal
import org.example.data.FoodCsvRepository
import org.example.logic.MealsRepository
import org.example.logic.SearchByAddDateUseCase
import java.io.File

/*
8- Search Foods by Add Date: Use Kotlin’s Date class to represent the date in the meal entity.
    Let the user input a date and return a list of IDs and names of meals added on that date.
    The user should be able to view details of a specific meal by entering its ID. Handle exceptions for:
        - Incorrect date format.
        - No meals were found for the given date. Ensure different exceptions are used for both cases.
 */

class SearchByAddDateUI {
    private val repository: MealsRepository = FoodCsvRepository(
        foodCsvReader = FoodCsvReader(
            csvFile = File("food.csv")
        ),
        foodCsvParser = FoodCsvParser()
    )

    private val useCase = SearchByAddDateUseCase(
        mealsRepository = repository
    )

    fun searchMealsByDate() {
        while (true) {
            println("📅 Enter date (YYYY-MM-DD):\n or 0 to exit")
            val date = readlnOrNull()

            if (date != null && date == "0") {
                return
            } else if (date != null && useCase.isValidDate(date)) {
                println("Loading...")
                searchFood(date)
            } else {
                println("Enter a valid Date or zero => 0")
            }
        }
    }

    private fun searchFood(date: String) {
        val filteredList = useCase.getMealsByDate(date)

        printMealsIdName(filteredList)

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

    private fun printMealsIdName(mealsList: List<Meal>) {
        mealsList.forEach { meal ->
            println("${meal.id} -> ${meal.name}")
        }
    }

    private fun viewMealDetails(mealsList: List<Meal>, mealId: Int) {
        val meal: Meal? = useCase.getMeal(mealsList, mealId)

        if (meal == null) {
            println("The meal with ID $mealId does not exist.")
            return
        }

        println(meal)
    }

}