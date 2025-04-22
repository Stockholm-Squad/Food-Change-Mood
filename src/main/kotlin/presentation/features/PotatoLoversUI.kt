package org.example.presentation.features

import org.example.logic.usecases.GetPotatoMealsUseCase

class PotatoLoversUI(private val getPotatoMealsUseCase: GetPotatoMealsUseCase) {

    fun showPotatoLoversUI() {
        println("🥔 I ❤️ Potato! Here are 10 meals that include potatoes:\n")
        showRandomPotatoMeals(10)
    }

    fun showRandomPotatoMeals(count: Int) {
        val result = getPotatoMealsUseCase.getRandomPotatoMeals(count)

        result
            .onSuccess { meals ->
                if (meals.isNotEmpty()) {
                    meals.forEachIndexed { index, meal ->
                        println("🍽️ Meal #${index + 1}: ${meal.name}")
                    }
                } else {
                    println("No potato meals found.")
                }
                askForMoreMeals()
            }
            .onFailure { exception ->
                println("❌ Failed to fetch potato meals: ${exception.message}")
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
