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
            askToViewMealDetails(meals)
            askIfWantsMore()
        }
    }

    fun handleFailure(exception: Throwable) {
        outputPrinter.printLine("❌ Error: ${exception.message}")
    }

    fun askToViewMealDetails(meals: List<Meal?>) {
        var validInput = false
        do {
            outputPrinter.printLine("\nWould you like to view the details of any of these meals? (Enter the number or 'n' to skip):")

            val input = try {
                inputReader?.readLineOrNull()?.trim()?.lowercase()
            } catch (e: Exception) {
                outputPrinter.printLine("Error reading input: ${e.message}")
                return
            }

            if (input == null || input == "n") {
                outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋")
                return
            }

            val selectedIndex = input.toIntOrNull()
            if (selectedIndex != null && selectedIndex in 1..meals.size) {
                meals[selectedIndex - 1]?.let { showMealDetails(it) }
                validInput = true  // Set the flag to true to exit the loop
            } else {
                outputPrinter.printLine("Invalid selection. Please choose a valid number.")
            }
        } while (!validInput)
    }

    fun showMealDetails(meal: Meal) {
        outputPrinter.printLine("\n🍽️ Details of '${meal.name}':")
        outputPrinter.printLine("🕒 Minutes to prepare: ${meal.minutes}")
        outputPrinter.printLine("📖 Number of steps: ${meal.numberOfSteps}")

        outputPrinter.printLine("📝 Steps:")
        meal.steps?.forEachIndexed { index, step ->
            outputPrinter.printLine("   ${index + 1}. $step")
        }

        outputPrinter.printLine("📃 Description: ${meal.description}")
        outputPrinter.printLine("🍎 Nutrition: ${meal.nutrition}")
        outputPrinter.printLine("🥣 Number of ingredients: ${meal.numberOfIngredients}")

        outputPrinter.printLine("🧂 Ingredients:")
        meal.ingredients?.forEachIndexed { index, ingredient ->
            outputPrinter.printLine("   ${index + 1}. $ingredient")
        } ?: outputPrinter.printLine("   N/A")
    }

    fun askIfWantsMore(onYes: () -> Unit = { showPotatoLoversUI() }) {
        outputPrinter.printLine("Would you like to see more? (y/n)")
        val input = try {
            inputReader?.readLineOrNull()
        } catch (e: Exception) {
            outputPrinter.printLine("❌ Error: ${e.message}")
            return
        }

        val normalizedAnswer = normalizeInput(input)

        if (normalizedAnswer == "y") {
            onYes()
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