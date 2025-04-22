package org.example.presentation.features

import org.example.logic.usecases.GetIngredientGameUseCase
import org.example.logic.usecases.model.IngredientQuestionModel

class IngredientGameUI(private val game: GetIngredientGameUseCase) {
    companion object {
        private var SCORE = 0
        private const val POINTS_PER_QUESTION = 1000
        private const val WINNING_SCORE = 15000
    }

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
            val isCorrect = game.submitAnswer(selectedIngredient)

            if (isCorrect) {
                SCORE += POINTS_PER_QUESTION
                println("✅ Correct! Your current score: $SCORE")
                println("--------------------------------------------------")

                if (SCORE >= WINNING_SCORE) {
                    println("\n🏁 Congratulations! You reached the winning score: $SCORE")
                    break
                }
            } else {
                println("🏁 Game Over! Final Score: $SCORE")
                break
            }
        }
    }

    private fun displayQuestion(question: IngredientQuestionModel) {
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