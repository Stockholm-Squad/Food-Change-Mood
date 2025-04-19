package org.example.logic.usecases

import model.Meal
import org.example.logic.repository.MealsRepository

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

    fun startIngredientGame(): Question {
        val meal = getRandomValidMeal()
        val correctIngredient = meal.ingredients!!.random() + " true"
        currentQuestion = Question(
            mealName = meal.name.toString(),
            options = generateOptions(correctIngredient),
            correctIngredient = correctIngredient
        )
        return currentQuestion
    }

    fun submitAnswer(selectedIngredient: String): GameState {
        return if (isGameOver()) gameState
        else updateGameState(selectedIngredient)
    }

    private fun isGameOver(): Boolean {
        return gameState.isGameOver
    }

    private fun updateGameState(selectedIngredient: String): GameState =
        gameState.apply {
            val isCorrect = selectedIngredient == currentQuestion.correctIngredient
            score += if (isCorrect) 1000 else 0
            correctAnswers += if (isCorrect) 1 else 0
            isGameOver = !isCorrect || correctAnswers >= 15
        }


    private fun getRandomValidMeal(): Meal =
        repository.getAllMeals()
            .filter { !it.ingredients.isNullOrEmpty() && !it.name.isNullOrEmpty() }
            .takeIf { it.isNotEmpty() }
            ?.random()
            ?: throw IllegalStateException("No valid meals available")

    private fun generateOptions(correctIngredient: String): List<String> =
        (repository.getAllMeals()
            .asSequence()
            .flatMap { it.ingredients.orEmpty().asSequence() }
            .filter { it != correctIngredient }
            .distinct()
            .shuffled()
            .take(2) + correctIngredient)
            .shuffled().toList()
}
