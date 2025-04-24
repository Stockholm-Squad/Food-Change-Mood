package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealsByDateUseCase
import org.example.utils.DateValidator
import org.example.utils.viewMealInListDetails

class SearchByAddDateUI(
    private val getMealsByDateUseCase: GetMealsByDateUseCase,
    private val dateValidator: DateValidator,
    private val reader: InputReader,
    private val printer: OutputPrinter
) {

    fun searchMealsByDate() {
        while (true) {
            printer.printLine("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02\n or 0 to exit")
            val date = reader.readLine()

            if (date == "0") {
                return
            } else if (dateValidator.isValidDate(date)) {
                printer.printLine("Loading...")
                searchFood(date)
            } else {
                printer.printLine("Enter a valid Date or zero => 0")
            }
        }
    }

    private fun searchFood(date: String) {
        getMealsByDateUseCase.getMealsByDate(date).fold(
            onSuccess = { meals -> meals },
            onFailure = { exception ->
                printer.printLine(("error: " + exception.message) ?: "An error occurred")
                emptyList()
            }
        ).also {
            handleUserInteraction(it)
        }
    }

    private fun printMealsIdName(mealsList: List<Meal>) {
        if (mealsList.isEmpty()) {
            printer.printLine("No meals found.")
        }

        mealsList.forEach { meal ->
            printer.printLine("${meal.id} -> ${meal.name}")
        }
    }

    private fun handleUserInteraction(meals: List<Meal>) {
        printMealsIdName(meals)

        while (true) {
            printer.printLine("")
            printer.printLine("-1 -> search again or back")
            printer.printLine("meal id -> view details")
            val input = reader.readLine()

            when (val mealId = input.toIntOrNull()) {
                null -> printer.printLine("Enter a valid ID or -1")
                -1 -> break
                else -> meals.viewMealInListDetails(mealId, printer)
            }
        }
    }
}