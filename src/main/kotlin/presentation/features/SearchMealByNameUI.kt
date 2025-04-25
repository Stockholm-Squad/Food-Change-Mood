package org.example.presentation.features

import org.example.logic.usecases.GetMealByNameUseCase
import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.utils.Constants

class SearchMealByNameUI(
    private val getMealByNameUseCase: GetMealByNameUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter
) {

    fun handleSearchByName() {
        printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH)
        reader.readStringOrNull()?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { pattern ->
                getMealByNameUseCase.getMealByName(pattern)
                    .onSuccess(::displayMeals)
                    .onFailure { printer.printLine(it.message) }
            }
            ?: printer.printLine(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY)
        askForMoreMeals()
    }

    private fun displayMeals(meals: List<Meal>) {
        meals.takeIf { it.isNotEmpty() }?.let {
            printer.printLine(Constants.FOUND_MEALS.format(it.size))
            it.forEachIndexed { index, meal ->
                printer.printLine("${index + 1}. ${meal.name}")
            }
            askToViewMealDetails(it)
        } ?: printer.printLine(Constants.NO_MEALS_FOUND_MATCHING)
    }

    private fun askToViewMealDetails(meals: List<Meal>) {
        printer.printLine(Constants.MEAL_DETAILS_PROMPT)
        reader.readStringOrNull()?.trim()?.lowercase()
            ?.takeIf { it != Constants.N }
            ?.let { input ->
                input.toIntOrNull()
                    ?.takeIf { it in 1..meals.size }
                    ?.let { showMealDetails(meals[it - 1]) }
                    ?: printer.printLine(Constants.INVALID_SELECTION_MESSAGE)
            }
    }

    private fun showMealDetails(meal: Meal) {
        printer.printLine(Constants.MEAL_DETAILS_HEADER.format(meal.name))
        printer.printMeal(meal)
    }

    private fun askForMoreMeals() {
        printer.printLine(Constants.SEARCH_AGAIN_PROMPT)
        val response = reader.readStringOrNull()?.trim()?.lowercase()
        if (response == Constants.Y) {
            handleSearchByName()
        } else {
            printer.printLine(Constants.GOODBYE_MESSAGE)
        }
    }
}
