package org.example.presentation.features

import model.Meal
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetIraqiMealsUseCase

class GetIraqiMealsUI(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val printer: OutputPrinter,
) {
    fun showIraqiMeals() {
        getIraqiMealsUseCase.getIraqiMeals().fold(
            onSuccess = { meal -> handleSuccess(meal) },
            onFailure = ::handleFailure
        )
    }

    private fun handleFailure(exception: Throwable) {
        printer.printLine(exception.message)
    }

    private fun handleSuccess(meals: List<Meal>) {
        meals.forEach { iraqiMeal ->
            printer.printLine("🍽 Ready for some amazing Iraqi meals? Let's go!")
            printer.printLine("Name: ${iraqiMeal.name}")
            printer.printLine("Time: ${iraqiMeal.minutes}")
            printer.printLine("Description: ${iraqiMeal.description ?: "No description available"}")
            printer.printLine("------------------------------------------------------------------------------")

        }
    }


}