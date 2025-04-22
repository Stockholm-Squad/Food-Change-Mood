package org.example.presentation.features

import org.example.logic.usecases.GetPotatoMealsUseCase

class PotatoLoversUI(private val getPotatoMealsUseCase: GetPotatoMealsUseCase) {

    fun showPotatoLoversUI() {
        println("🥔 I ❤️ Potato! Here are 10 meals that include potatoes:\n")
        showRandomPotatoMeals(10)
    }

    fun showRandomPotatoMeals(count: Int) {
        getPotatoMealsUseCase.getRandomPotatoMeals(count)
            .onSuccess { meals ->
                meals.takeIf { it.isNotEmpty() }
                    ?.forEachIndexed { index, meal ->
                        println("🍽️ Meal #${index + 1}: ${meal.name}")
                    }
                    ?: run { println("No potato meals found.") }
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
        readlnOrNull()?.trim()?.lowercase()
            .takeIf { it == "y" }
            ?.let { showRandomPotatoMeals(10) }
            ?: println("Okay! Enjoy your potato meals! 🥔😋")
    }
}
