package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.presentation.features.utils.ConsoleMealDisplayer
import org.example.presentation.features.utils.SearchUtils
import org.example.utils.Constants

class PotatoLoversUI(
    private val getPotatoMealsUseCase: GetPotatoMealsUseCase,
    private val printer: OutputPrinter,
    private val reader: InputReader,
    private val searchUtils: SearchUtils = SearchUtils(),
    private val mealDisplayer: ConsoleMealDisplayer = ConsoleMealDisplayer(printer)
) {

    fun showPotatoLoversUI(count: Int = 10) {
        val meals = getPotatoMealsUseCase.getRandomPotatoMeals(count).getOrNull().orEmpty()
        printer.printLine("${Constants.I_LOVE_POTATO_HERE}$count${Constants.MEAL_INCLUDE_POTATO}\n")

        if (meals.isEmpty()) {
            printer.printLine(Constants.NO_MEALS_FOUND)
            return
        }

        interactWithMealSelection(meals)
    }

    private fun interactWithMealSelection(meals: List<Meal>) {
        generateSequence {
            askToViewMealDetails(meals)
            searchUtils.shouldSearchAgain(reader)
        }.takeWhile { it }.toList()
    }

    private fun askToViewMealDetails(meals: List<Meal>) {
        printer.printLine("\n${Constants.MEAL_DETAILS_PROMPT}")
        val selectedIndex = searchUtils.getValidMealIndex(reader, meals.size)
        if (selectedIndex != null) meals[selectedIndex].let(::showMealDetails) else {
            printer.printLine(Constants.INVALID_SELECTION_MESSAGE)
        }
    }

    private fun showMealDetails(meal: Meal) {
        mealDisplayer.display(meal)
    }
}
