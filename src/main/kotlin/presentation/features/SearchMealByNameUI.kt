package org.example.presentation.features

import org.example.logic.usecases.GetMealByNameUseCase

class SearchMealByNameUI(private val getMealByNameUseCase: GetMealByNameUseCase) {

    fun handleSearchByName() {
        print("🔍 Enter a meal keyword to search: ")
        val name = readlnOrNull()?.trim()

        val result = getMealByNameUseCase.getMealByName(name)

        result
            .onSuccess { meals ->
                println("✅ Found ${meals.size} meal(s) matching '$name':")
                meals.forEach { meal ->
                    println("- ${meal.name}")
                }
            }
            .onFailure { exception ->
                println("❌ ${exception.message}")
            }

        askForMoreMeals()
    }

    private fun askForMoreMeals() {
        println(
            "------------------------------------------------\n" +
                    "Would you like to search again? (y or n)"
        )
        if (readlnOrNull()?.trim()?.lowercase() == "y") {
            handleSearchByName()
        } else {
            println("Okay! Enjoy your meals! 😋")
        }
    }
}
