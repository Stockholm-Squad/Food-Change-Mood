package org.example.presentation.features.utils

import model.Meal
import org.example.input_output.output.OutputPrinter
import org.example.utils.Constants

class ConsoleMealDisplayer(private val printer: OutputPrinter) {

    fun display(meal: Meal?) {
        meal?.run {
            name?.let { printMealHeader(it) }
            printer.printMeal(this)
        } ?: printer.printLine("No meal to display.")
    }

    private fun printMealHeader(name: String) {
        printer.printLine(Constants.MEAL_DETAILS_HEADER.format(name))
    }

}