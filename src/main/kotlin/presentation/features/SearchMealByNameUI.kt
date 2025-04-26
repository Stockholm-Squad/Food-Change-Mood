package org.example.presentation.features

import org.example.logic.usecases.GetMealByNameUseCase
import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.presentation.features.utils.ConsoleMealDisplayer
import org.example.presentation.features.utils.SearchUtils
import org.example.utils.Constants

class SearchMealByNameUI(
    private val getMealByNameUseCase: GetMealByNameUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter,
    private val mealDisplayer: ConsoleMealDisplayer,
    private val searchUtils: SearchUtils

) {
    fun handleSearchByName() {
        printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH)
        searchUtils.readNonBlankTrimmedInput(reader)
            ?.let(::fetchAndDisplayMeals)
            ?: printer.printLine(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY)

        askForMoreMeals()
    }

    private fun fetchAndDisplayMeals(pattern: String) =
        getMealByNameUseCase.getMealByName(pattern)
            .onSuccess(::displayMeals)
            .onFailure { exception ->
                this@SearchMealByNameUI.handleFailure(exception)
            }

    private fun displayMeals(meals: List<Meal>) =
        meals.takeIf(List<Meal>::isNotEmpty)
            ?.let {
                printer.printLine(Constants.FOUND_MEALS.format(it.size))
                it.forEachIndexed { index, meal ->
                    printer.printLine("${index + 1}. ${meal.name}")
                }
                askToViewMealDetails(it)
            }
            ?: printer.printLine(Constants.NO_MEALS_FOUND_MATCHING)

    private fun askToViewMealDetails(meals: List<Meal>) =
        printer.printLine(Constants.MEAL_DETAILS_PROMPT).also {
            searchUtils.getValidMealIndex(reader, meals.size)
                ?.let { index -> showMealDetails(meals[index]) }
                ?: printer.printLine(Constants.INVALID_SELECTION_MESSAGE)
        }

    private fun showMealDetails(meal: Meal) {
        mealDisplayer.display(meal)
    }

    private fun askForMoreMeals() =
        searchUtils.shouldSearchAgain(reader)
            ?.let { handleSearchByName() }
            ?: printer.printLine(Constants.GOODBYE_MESSAGE)


    private fun handleFailure(exception: Throwable) {
        printer.printLine(exception.message)
    }

}
