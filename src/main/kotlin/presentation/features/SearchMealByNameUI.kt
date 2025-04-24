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
        printer.printLine("🔍 Enter a meal keyword to search: ")
        reader.readLineOrNull()?.trim()
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
            it.forEachIndexed { index, meal -> println("${index + 1}. ${meal.name}") }
            askToViewMealDetails(it)
        } ?: printer.printLine(Constants.NO_MEALS_FOUND_MATCHING)
    }


    private fun askToViewMealDetails(meals: List<Meal>) {
        printer.printLine("\nWould you like to view the details of any of these meals? (Enter the number or 'n' to skip):")
        reader.readLineOrNull()?.trim()?.lowercase()
            ?.takeIf { it != "n" }
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
        reader.readLineOrNull()?.trim()?.lowercase()
            ?.takeIf { it == "y" }
            ?.let {
                handleSearchByName()
            }
            ?: printer.printLine(Constants.GOODBYE_MESSAGE)
    }
}
