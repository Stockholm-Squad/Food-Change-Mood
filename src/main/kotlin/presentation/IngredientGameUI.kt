package org.example.presentation

import org.example.logic.usecases.IngredientGameUseCase

class IngredientGameUI(private val game: IngredientGameUseCase) {

    fun start() {
        println("🧠 Ingredient Game is starting! Let's see how well you know your meals!")
        println("Guess the correct ingredient for each meal.")
        println("Get 15 right to win! One wrong answer ends the game.")
        println("--------------------------------------------------")

        while (true) {
            val question = game.startIngredientGame()
            displayQuestion(question)

            val userChoice = getUserChoice(question.options.size)

            val selectedIngredient = question.options[userChoice - 1]
            val gameState = game.submitAnswer(selectedIngredient)

            if (gameState.isGameOver) {
                println("\n🏁 Game Over! Final Score: ${gameState.score}")
                break
            } else {
                println("✅ Correct! Your current score: ${gameState.score}")
                println("--------------------------------------------------")
            }
        }
    }

    private fun displayQuestion(question: IngredientGameUseCase.Question) {
        println("\nMeal: ${question.mealName}")
        println("Choose the correct ingredient:")

        question.options.forEachIndexed { index, option ->
            println("${index + 1}. $option")
        }
    }

    private fun getUserChoice(optionCount: Int): Int {
        while (true) {
            print("Enter your choice (1-$optionCount): ")
            val input = readln()
            val choice = input.toIntOrNull()

            if (choice != null && choice in 1..optionCount) {
                return choice
            }
            println("❗ Invalid input. Please enter a number between 1 and $optionCount.")
        }
    }
}

