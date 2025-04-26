package org.example.presentation.features.utils

import model.Meal
import org.example.input_output.output.OutputPrinter
import org.example.utils.Constants

class ConsoleMealDisplayer(private val printer: OutputPrinter) {

    fun display(meal: Meal) {
        meal.name?.let {
            printMealHeader(it)
        }
        printer.printMeal(meal)
    }

    private fun printMealHeader(name: String) {
        printer.printLine(Constants.MEAL_DETAILS_HEADER.format(name))
    }
}