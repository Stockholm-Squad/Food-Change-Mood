package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealForSoThinPeopleUseCase
import org.example.utils.Constants


class SuggestMealForSoThinPeopleUI(
    private val getMealForSoThinPeopleUseCase: GetMealForSoThinPeopleUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter,
) {
    fun showMealWithHighCalorie() {
        getMealForSoThinPeopleUseCase.suggestRandomMealForSoThinPeople()
            .fold(
                onSuccess = {meal -> handleSuccess(meal)},
                onFailure =::handleFailure
            )
    }
    private fun handleFailure(exception: Throwable){
        printer.printLine(exception.message)
    }
    private fun handleSuccess(meal:Meal){
        printer.printLine("-------------------------------------------------------------------------------------------------")
        printer.printLine("🔥 Feeling too thin? This meal above 700 calories!")
        printer.printLine("------------------------------------------------------------------------------------------------")
        printer.printLine("Name: " + meal.name)
        printer.printLine("Description: " + meal.description)
        printer.printLine("--------------------------------------------------------------------------------------------------")
        printer.printLine("Do you like it? (yes/no) 😊")
        val input = reader.readStringOrNull().takeIf {it != null}?: printer.printLine(Constants.INVALID_INPUT)
        when (input.toString().lowercase().trim()){
            "no" -> showMealWithHighCalorie()
            "yes" -> showMealDetails(meal)
            else -> printer.printLine(Constants.INVALID_INPUT)
        }
    }
    private fun showMealDetails(meal: Meal) {
        printer.printLine("Name: " + meal.name)
        printer.printLine("Description: " + meal.description)
        printer.printLine("Preparation Time: " + meal.minutes + " minutes")
        printer.printLine("Nutrition: " + meal.nutrition)

    }
}