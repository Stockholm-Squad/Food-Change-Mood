package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealsForGymHelperUseCase
import org.example.utils.Constants

class GymHelperUI(
    private val getMealsForGymHelperUseCase: GetMealsForGymHelperUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter,
) {

    fun useGymHelper() {
        this.getDesiredCalories()?.let { desiredCalories ->
            this@GymHelperUI.getDesiredProteins()?.let { desiredProteins ->
                getMealsForGymHelperUseCase.getGymHelperMeals(
                    desiredCalories = desiredCalories,
                    desiredProteins = desiredProteins
                ).fold(
                    onSuccess = { gymHelperMeals ->
                        this@GymHelperUI.handleSuccess(gymHelperMeals)
                    },
                    onFailure = { exception ->
                        this@GymHelperUI.handleFailure(exception)
                    }
                )
            } ?: showInvalidInput()
        } ?: showInvalidInput()
    }

    private fun getDesiredProteins(): Float? {
        printer.printLine("🔥 Enter desired proteins: ")
        return reader.readFloatOrNull()
    }

    private fun getDesiredCalories(): Float? {
        printer.printLine("🔥 Enter desired calories: ")
        return reader.readFloatOrNull()
    }

    private fun handleFailure(exception: Throwable) {
        printer.printLine(exception.message)
    }

    private fun handleSuccess(gymHelperMeals: List<Meal>) {
        printer.printMeals(gymHelperMeals)
    }

    private fun showInvalidInput() {
        printer.printLine(Constants.INVALID_INPUT)
    }

}