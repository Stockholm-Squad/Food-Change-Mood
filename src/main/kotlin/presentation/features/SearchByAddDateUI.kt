package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetMealsByDateUseCase
import org.example.utils.DateValidator
import org.example.utils.viewMealInListDetails

class SearchByAddDateUI(
    private val getMealsByDateUseCase: GetMealsByDateUseCase,
    private val dateValidator: DateValidator
) {

    fun searchMealsByDate() {
        while (true) {
            println("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02\n or 0 to exit")
            val date = readln()

            if (date == "0") {
                return
            } else if (dateValidator.isValidDate(date)) {
                println("Loading...")
                searchFood(date)
            } else {
                println("Enter a valid Date or zero => 0")
            }
        }
    }

    private fun searchFood(date: String) {
        getMealsByDateUseCase.getMealsByDate(date).fold(
            onSuccess = { meals -> meals },
            onFailure = { exception ->
                println(exception)
                emptyList()
            }
        ).also {
            handleUserInteraction(it)
        }
    }


    private fun printMealsIdName(mealsList: List<Meal>) {
        mealsList.forEach { meal ->
            println("${meal.id} -> ${meal.name}")
        }
    }

    private fun handleUserInteraction(meals: List<Meal>) {
        printMealsIdName(meals)

        while (true) {
            println()
            println("-1 -> search again or back")
            println("meal id -> view details")
            val input: String = readln()

            when (val mealId: Int? = input.toIntOrNull()) {
                null -> println("Enter a valid ID or -1")
                -1 -> break
                else -> meals.viewMealInListDetails(mealId)
            }
        }
    }
}