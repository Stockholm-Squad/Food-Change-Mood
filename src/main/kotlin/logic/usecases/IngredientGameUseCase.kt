package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

//TODO Make the game more readable and try to use functional programming
class IngredientGameUseCase(
    private val repository: MealsRepository
) {

    data class Question(
        val mealName: String,
        val options: List<String>,
        val correctIngredient: String
    )

    data class GameState(
        var score: Int = 0,
        var correctAnswers: Int = 0,
        var isGameOver: Boolean = false
    )


    private val gameState = GameState()
    private lateinit var currentQuestion: Question
    //TODO rename the function name to be ex: showIngredientGame
    fun startNewRound(): Question {
        val meal = getValidRandomMeal()
        val correctIngredient = meal.ingredients!!.random() + " true"

        val incorrectIngredients = generateIncorrectIngredients(correctIngredient)
        val options = (incorrectIngredients + correctIngredient).shuffled()
        currentQuestion = Question(
            mealName = meal.name.toString(),
            options = options,
            correctIngredient = correctIngredient
        )

        return currentQuestion
    }

    fun submitAnswer(selectedIngredient: String): GameState {
        if (gameState.isGameOver) return gameState
        println("${currentQuestion.correctIngredient} $selectedIngredient")
        val isCorrect = currentQuestion.correctIngredient == selectedIngredient

        if (isCorrect) {
            gameState.score += 1000
            gameState.correctAnswers += 1

            if (gameState.correctAnswers >= 15) {
                gameState.isGameOver = true
            }
        } else {
            gameState.isGameOver = true
        }

        return gameState
    }

    private fun getValidRandomMeal(): Meal {
        val meals = repository.getAllMeals().filter { !it.ingredients.isNullOrEmpty() && !it.name.isNullOrEmpty()}
        if (meals.isEmpty()) throw IllegalStateException("No valid meals available")
        val meal = meals.random()
        return meal
    }

    private fun generateIncorrectIngredients(correctIngredient: String): List<String> {
        val allIngredients = repository.getAllMeals()
            .flatMap { it.ingredients.orEmpty() }
            .filter { it != correctIngredient }
            .distinct()

        return allIngredients.shuffled().take(2)
    }
}
