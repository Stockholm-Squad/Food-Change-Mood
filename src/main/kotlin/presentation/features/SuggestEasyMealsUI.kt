package org.example.presentation.features

import model.Meal
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetEasyFoodSuggestionsUseCase
import org.example.utils.Constants

class SuggestEasyMealsUI(
    private val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase,
    private val printer: OutputPrinter
) {

    fun showEasySuggestions() {
        printer.printLine("⏱️ Easy meals coming up!")
        getEasyFoodSuggestionsUseCase.getEasyFood().fold(
            onSuccess = { meals -> this.handleSuccess(meals) },
            onFailure = { exception -> this.handleFailure(exception) }
        )
    }

    private fun handleSuccess(meals: List<Meal>) {
        meals.takeIf { it.isNotEmpty() }?.let { printer.printMeals(it) }
            ?: printer.printLine(Constants.NO_EASY_MEALS_FOUND)
    }

    private fun handleFailure(exception: Throwable) {
        printer.printLine(exception.message)
    }
}