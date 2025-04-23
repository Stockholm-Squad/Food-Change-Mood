package org.example.presentation.features

import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.utils.InputHandler
import org.example.utils.OutputHandler

class PotatoLoversUI(
    private val getPotatoMealsUseCase: GetPotatoMealsUseCase,
    private val outputHandler: OutputHandler,
    private val inputHandler: InputHandler? = null
) {
    fun showPotatoLoversUI(count: Int = 10) {
        val result = getPotatoMealsUseCase.getRandomPotatoMeals(count)
        if (result.isSuccess) {
            val meals = result.getOrNull()
            if (meals.isNullOrEmpty()) {
                outputHandler.showMessage("😢 No potato meals found.")
            } else {
                outputHandler.showMessage("🥔 I ❤️ Potato Meals:")
                meals.forEachIndexed { index, meal ->
                    outputHandler.showMessage("🍽️ Meal #${index + 1}: ${meal.name}")
                }
                askForMoreMeals()
            }
        } else {
            outputHandler.showMessage("❌ Error: ${result.exceptionOrNull()?.message}")
        }
    }

    private fun askForMoreMeals() {
        inputHandler?.let {
            outputHandler.showMessage(
                "------------------------------------------------\n" +
                        "Would you like to see more potato meals? (y or n)"
            )
            val answer = it.readInput()?.trim()?.lowercase()
            if (answer == "y") {
                showPotatoLoversUI()
            } else {
                outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋")
            }
        }
    }
}