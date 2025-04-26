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
    private val searchUtils: SearchUtils,
    private val mealDisplayer: ConsoleMealDisplayer = ConsoleMealDisplayer(printer)
) {

    fun showPotatoLoversUI(count: Int = 10) {
        var shouldContinue: Boolean

        do {
            val meals = fetchPotatoMeals(count)
            if (meals.isEmpty()) return

            displayMealsIntro(count)
            displayMealsList(meals)
            askToViewMealDetails(meals)

            shouldContinue = searchUtils.shouldSearchAgain(reader) == true
        } while (shouldContinue)
    }


    private fun fetchPotatoMeals(count: Int): List<Meal> {
        val meals = getPotatoMealsUseCase.getRandomPotatoMeals(count).getOrNull().orEmpty()
        if (meals.isEmpty()) {
            printer.printLine(Constants.NO_MEALS_FOUND)
        }
        return meals
    }

    private fun displayMealsIntro(count: Int) {
        printer.printLine("${Constants.I_LOVE_POTATO_HERE}$count${Constants.MEAL_INCLUDE_POTATO}\n")
    }

    private fun displayMealsList(meals: List<Meal>) {
        printer.printLine("Available meals:")
        meals.forEachIndexed { index, meal ->
            printer.printLine("${index + 1}. ${meal.name}")
        }
    }

    private fun askToViewMealDetails(meals: List<Meal>) {
        printer.printLine("\n${Constants.MEAL_DETAILS_PROMPT}")
        val index = searchUtils.getValidMealIndex(reader, meals.size)
        if (index != null) {
            mealDisplayer.display(meals[index])
        } else {
            printer.printLine(Constants.SKIPPING_MEAL_DETAILS)
        }
    }
}