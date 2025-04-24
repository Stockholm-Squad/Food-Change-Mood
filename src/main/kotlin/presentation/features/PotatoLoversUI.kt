package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.utils.InputHandler
import org.example.utils.OutputHandler

class PotatoLoversUI(
private val getPotatoMealsUseCase: GetPotatoMealsUseCase,
private val outputHandler: OutputHandler,
private val inputHandler: InputHandler? = null
) {

    fun showPotatoLoversUI(count: Int = 10) {
        outputHandler.showMessage("🥔 I ❤️ Potato! Here are $count meals that include potatoes:\n")
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
            outputHandler.showMessage("😢 No potato meals found.")
        } else {
            outputHandler.showMessage("🥔 I ❤️ Potato Meals:")
            meals.forEachIndexed { index, meal ->
                outputHandler.showMessage("🍽️ Meal #${index + 1}: ${meal.name}")
            }
            askIfWantsMore()
        }
    }

    fun handleFailure(exception: Throwable) {
        outputHandler.showMessage("❌ Error: ${exception.message}")
    }

    private fun askIfWantsMore() {
        outputHandler.showMessage("Would you like to see more? (y/n)")
        val input = try {
            inputHandler?.readInput()
        } catch (e: Exception) {
            outputHandler.showMessage("❌ Error: ${e.message}")
            return
        }

        val normalizedAnswer = normalizeInput(input)

        if (normalizedAnswer == "y") {
            showPotatoLoversUI()
        } else {
            outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋")
        }
    }

    companion object {
        fun normalizeInput(input: String?): String {
            if (input == null) return ""
            return input.trim().lowercase()
        }
    }
}