package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.utils.InputReader
import org.example.utils.OutputPrinter

class PotatoLoversUI(
    private val getPotatoMealsUseCase: GetPotatoMealsUseCase,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader? = null
) {

    fun showPotatoLoversUI(count: Int = 10) {
        outputPrinter.printLine("🥔 I ❤️ Potato! Here are $count meals that include potatoes:\n")
        getPotatoMealsUseCase.getRandomPotatoMeals(count).fold(
            onSuccess = { meals ->
                handleSuccess(meals)
            },
            onFailure = { exception ->
                handleFailure(exception)
            }
        )
    }

    fun handleSuccess(meals: List<Meal>) {
        if (meals.isEmpty()) {
            outputPrinter.printLine("😢 No potato meals found.")
        } else {
            outputPrinter.printLine("🥔 I ❤️ Potato Meals:")
            meals.forEachIndexed { index, meal ->
                outputPrinter.printLine("🍽️ Meal #${index + 1}: ${meal.name}")
            }
            askIfWantsMore()
        }
    }

    fun handleFailure(exception: Throwable) {
        outputPrinter.printLine("❌ Error: ${exception.message}")
    }

    private fun askIfWantsMore() {
        outputPrinter.printLine("Would you like to see more? (y/n)")
        val input = try {
            inputReader?.readLineOrNull()
        } catch (e: Exception) {
            outputPrinter.printLine("❌ Error: ${e.message}")
            return
        }

        val normalizedAnswer = normalizeInput(input)

        if (normalizedAnswer == "y") {
            showPotatoLoversUI()
        } else {
            outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋")
        }
    }

    companion object {
        fun normalizeInput(input: String?): String {
            if (input == null) return ""
            return input.trim().lowercase()
        }
    }
}