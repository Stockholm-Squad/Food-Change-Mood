package org.example.presentation.features

import org.example.logic.model.FoodChangeModeResults
import org.example.logic.usecases.GetPotatoMealsUseCase

class PotatoLoversUI(private val getPotatoMealsUseCase: GetPotatoMealsUseCase) {

    fun showPotatoLoversUI() {
        println("🥔 I ❤️ Potato! Here are 10 meals that include potatoes:\n")
        showRandomPotatoMeals(10)
    }

    fun showRandomPotatoMeals(count: Int) {
        when (val result = getPotatoMealsUseCase.getRandomPotatoMeals(count)) {
            is FoodChangeModeResults.Success -> {
                if (result.model.isNotEmpty()) {
                    result.model.forEachIndexed { index, meal ->
                        println("🍽️ Meal #${index + 1}: ${meal.name}")

                        askForMoreMeals()
                    }
                } else {
                    println("No potato meals found.")
                }
            }
            is FoodChangeModeResults.Fail -> {
                println("Failed to fetch potato meals: ${result.exception.message}")
            }
        }
    }

    private fun askForMoreMeals() {
        println(
            "------------------------------------------------\n" +
                    "Would you like to see more potato meals? (y or n)"
        )
        if (readlnOrNull()?.trim()?.lowercase() == "y") {
            showRandomPotatoMeals(10)
        } else {
            println("Okay! Enjoy your potato meals! 🥔😋")
        }
    }
}