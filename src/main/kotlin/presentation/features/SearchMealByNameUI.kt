package org.example.presentation.features

import org.example.logic.usecases.GetMealByNameUseCase
import model.Meal

class SearchMealByNameUI(private val getMealByNameUseCase: GetMealByNameUseCase) {

    fun handleSearchByName() {
        print("🔍 Enter a meal keyword to search: ")
        readlnOrNull()?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { query ->
                getMealByNameUseCase.getMealByName(query)
                    .onSuccess(::displayMeals)
                    .onFailure { println("❌ ${it.message}") }
            }
            ?: println("❌ Search query cannot be empty.")

        askForMoreMeals()
    }

    private fun displayMeals(meals: List<Meal>) {
        if (meals.isEmpty()) {
            println("❌ No meals found matching the query.")
        } else {
            println("✅ Found ${meals.size} meal(s):")
            meals.forEachIndexed { index, meal -> println("${index + 1}. ${meal.name}") }
            askToViewMealDetails(meals)
        }
    }

    private fun askToViewMealDetails(meals: List<Meal>) {
        println("\nWould you like to view the details of any of these meals? (Enter the number or 'n' to skip):")
        readlnOrNull()?.trim()?.lowercase()
            ?.takeIf { it != "n" }
            ?.let { input ->
                input.toIntOrNull()
                    ?.takeIf { it in 1..meals.size }
                    ?.let { showMealDetails(meals[it - 1]) }
                    ?: println("❌ Invalid selection. Please choose a valid number.")
            }
    }

    private fun showMealDetails(meal: Meal) {
        println("\nHere are the details for '${meal.name}':")
        println(meal)
    }

    private fun askForMoreMeals() {
        println(
            "------------------------------------------------\n" +
                    "Would you like to search again? (y or n)"
        )
        readlnOrNull()?.trim()?.lowercase()
            ?.takeIf { it == "y" }
            ?.let { handleSearchByName() }
            ?: println("Okay! Enjoy your meals! 😋")
    }
}
