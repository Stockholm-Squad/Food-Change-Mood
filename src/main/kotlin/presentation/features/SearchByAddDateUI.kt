package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealsByDateUseCase
import org.example.utils.Constants.ENTER_VALID_DATE
import org.example.utils.Constants.NO_MEALS_FOUND_WITH_THIS_DATE
import org.example.utils.Constants.SEARCH_AGAIN_OR_BACK
import org.example.utils.DateValidator

class SearchByAddDateUI(
    private val getMealsByDateUseCase: GetMealsByDateUseCase,
    private val dateValidator: DateValidator,
    private val reader: InputReader,
    private val printer: OutputPrinter
) {

    fun searchMealsByDate() {
        while (true) {
            printer.printLine("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02\n or 0 to exit")
            reader.readStringOrNull().also {
                when{
                    it == null -> printer.printLine(ENTER_VALID_DATE)
                    it == "0" -> return
                    dateValidator.isValidDate(it) -> {
                        printer.printLine("Loading...")
                        searchFood(it)
                    }
                    else -> printer.printLine(ENTER_VALID_DATE)
                }
            }
        }
    }

    private fun searchFood(date: String) {
        getMealsByDateUseCase.getMealsByDate(date).fold(
            onSuccess = { meals -> meals },
            onFailure = { exception ->
                printer.printLine("error: " + (exception.message ?: "Unknown Error"))
                emptyList()
            }
        ).also {
            handleUserInteraction(it)
        }
    }

    private fun printMealsIdName(mealsList: List<Meal>) {
        if (mealsList.isEmpty()) {
            printer.printLine(NO_MEALS_FOUND_WITH_THIS_DATE)
        }

        mealsList.forEach { meal ->
            printer.printLine("${meal.id} -> ${meal.name}")
        }
    }

    private fun handleUserInteraction(meals: List<Meal>) {
        printMealsIdName(meals)

        while (true) {
            printer.printLine("")
            printer.printLine("-1 -> $SEARCH_AGAIN_OR_BACK")
            printer.printLine("meal id -> view details")
            val input = reader.readStringOrNull()

            if (viewMealDetails(input, meals)) break

        }
    }

    private fun viewMealDetails(input: String?, meals: List<Meal>): Boolean {
        when (val mealId = input?.toIntOrNull()) {
            null -> printer.printLine("Enter a valid ID or -1")
            -1 -> return true
            else -> viewMealInListDetails(mealId, meals, printer)
        }
        return false
    }

    fun viewMealInListDetails(mealId: Int, list: List<Meal>, printer: OutputPrinter) {
        val meal: Meal? = list.find { meal ->
            meal.id == mealId
        }

        if (meal == null) {
            printer.printLine("The meal with ID $mealId does not exist.")
            return
        }

        printer.printMeal(meal)
    }
}