package org.example.presentation.features

import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealForKetoDietUseCase

class KetoDietMealUI(
    private val getMealForKetoDietUseCase: GetMealForKetoDietUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter
) {

    fun showKetoMeal() {
        printer.printLine("🥑 Finding a keto-friendly meal for you...")

        getMealForKetoDietUseCase.getKetoMeal().fold(
            onSuccess = { meal ->
                if (meal == null) {
                    printer.printLine("✔️ You've seen all available keto meals!")
                    return
                }

                printer.printLine("🍽 Suggested Keto Meal: ${meal.name}")
                printer.printLine("📋 Description: ${meal.description}")
                printer.printLine("🧪 Nutrition -> Carbs: ${meal.nutrition?.carbohydrates}g, Fat: ${meal.nutrition?.totalFat}g, Protein: ${meal.nutrition?.protein}g")

                meal.name?.let { handleUserFeedback(it) }
            },
            onFailure = {
                printer.printLine("❌ Failed to load meals. Please try again later.")
            }
        )
    }

    private fun handleUserFeedback(mealName: String) {
        printer.printLine("\nDo you like this meal? (yes/no)")
        when (reader.readLineOrNull()?.trim()?.lowercase()) {
            "yes" -> printer.printLine("👍 Great, enjoy your meal: $mealName")
            "no" -> showKetoMeal()
            else -> {
                printer.printLine("⚠️ Invalid input. Please answer with 'yes' or 'no'.")
                handleUserFeedback(mealName)
            }
        }
    }
}
