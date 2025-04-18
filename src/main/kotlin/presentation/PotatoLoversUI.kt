package org.example.presentation

import org.example.logic.GetPotatoMealsUseCase

class PotatoLoversUI(private val getPotatoMealsUseCase: GetPotatoMealsUseCase) {

    fun showPotatoLoversUI() {
        println("🥔 I ❤️ Potato! Here are 10 meals that include potatoes:\n")
        displayMeals()
    }

    private fun displayMeals() {
        getPotatoMealsUseCase.getRandomPotatoMeals(10).forEach { meal ->
            println("🍽️ Meal Name: ${meal.name}")

        }
        askForMoreMeals()
    }

    private fun askForMoreMeals() {
        println(
            "------------------------------------------------\n" +
                    "Would you like to see more potato meals? (y or n)"
        )
        if (readlnOrNull()?.trim()?.lowercase() == "y") {
            displayMeals()
        } else {
            println("Okay! Enjoy your potato meals! 🥔😋")
        }
    }
}
