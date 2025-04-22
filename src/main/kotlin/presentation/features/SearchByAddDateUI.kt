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
            val date = readlnOrNull()

            if (date != null && date == "0") {
                return
            } else if (date != null && dateValidator.isValidDate(date)) {
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
            printMealsIdName(it)
        }.also {
            while (true) {
                println()
                println("-1 -> search again or back")
                println("meal id -> view details")
                val input = readlnOrNull()
                val mealId = input?.toIntOrNull()

                if (mealId == null) {
                    println("Enter a valid ID or -1")
                    continue
                } else if (mealId == -1) {
                    break
                } else {
                    it.viewMealInListDetails(mealId)
                }
            }
        }

    }

    private fun printMealsIdName(mealsList: List<Meal>) {
        mealsList.forEach { meal ->
            println("${meal.id} -> ${meal.name}")
        }
    }


}