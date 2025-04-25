package org.example.presentation.features

import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealForKetoDietUseCase
import org.example.utils.Constants

class KetoDietMealUI(
    private val getMealForKetoDietUseCase: GetMealForKetoDietUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter
) {

    fun showKetoMeal() {
        printer.printLine(Constants.START_MESSAGE)

        getMealForKetoDietUseCase.getKetoMeal().fold(
            onSuccess = { meal ->
                if (meal == null) {
                    printer.printLine(Constants.FINISH_MEALS)
                    return
                }

                printer.printLine("🍽 Suggested Keto Meal: ${meal.name}")
                printer.printLine("📋 Description: ${meal.description}")
                printer.printLine("🧪 Nutrition -> Carbs: ${meal.nutrition?.carbohydrates}g, Fat: ${meal.nutrition?.totalFat}g, Protein: ${meal.nutrition?.protein}g")

                meal.name?.let { handleUserFeedback(it) }
            },
            onFailure = {
                printer.printLine(Constants.ERROR_FETCHING_MEALS)
            }
        )
    }

    private fun handleUserFeedback(mealName: String) {
        printer.printLine(Constants.ASK_YES_NO)
        val input = reader.readLineOrNull()?.trim()?.lowercase()
        when {
            input == "yes" -> printer.printLine("${Constants.YES_ANSWER}$mealName")
            input == "no" -> showKetoMeal()
            input.isNullOrBlank() -> {
                printer.printLine(Constants.INVALID_INPUT)
                handleUserFeedback(mealName)
            }
            else -> {
                printer.printLine(Constants.INVALID_INPUT)
                handleUserFeedback(mealName)
            }
        }
    }


}
