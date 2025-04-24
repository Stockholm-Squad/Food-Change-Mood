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
        printer.printLine("-------------------------------------------------------------------------------------------------")
        printer.printLine("🔥 Feeling too thin? This meal above 700 calories!")
        getMealForSoThinPeopleUseCase.suggestRandomMealForSoThinPeople()
            .fold(
                onSuccess = { meal ->
                    printer.printLine("------------------------------------------------------------------------------------------------")
                    printer.printLine("Name: " + meal.name)
                    printer.printLine("Description: " + meal.description)
                    printer.printLine("--------------------------------------------------------------------------------------------------")
                    printer.printLine("Do you like it? (yes/no) 😊")
                    when (reader.readStringOrNull()?.lowercase()?.trim()) {
                        "no" -> showMealWithHighCalorie()
                        "yes" -> showMealDetails(meal)
                        else -> printer.printLine(Constants.INVALID_INPUT)
                    }
                },
                onFailure =::handleFailure
            )
    }
    private fun handleFailure(exception: Throwable){
        printer.printLine(exception.message)
    }
    private fun showMealDetails(meal: Meal) {
        printer.printLine("Meal Name: " + meal.name)
        printer.printLine("Meal Description: " + meal.description)
        printer.printLine("Meal Preparation Time: " + meal.minutes + " minutes")
        printer.printLine("Meal " + meal.nutrition)

    }
}