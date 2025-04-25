package org.example.presentation.features

import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetHealthyFastFoodUseCase
import org.example.utils.Constants

class GetHealthyFastFoodMealsUI(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase,
    private val printer: OutputPrinter,
) {



    fun showHealthyFastFoodMeals() {
        getHealthyFastFoodUseCase.getHealthyFastFood().fold(
            onSuccess = { allMeals ->
                printer.printMeals(allMeals)
            },
            onFailure = { error ->
                printer.printLine(error.message)
            }
        )
    }

}

