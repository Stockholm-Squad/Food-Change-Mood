package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetGuessPreparationTimeUseCase
import org.example.logic.usecases.GetRandomMealUseCase
import org.example.logic.usecases.model.GuessPreparationTimeState
import org.example.utils.Constants

class GuessGameUI(
    private val getGuessPreparationTimeUseCase: GetGuessPreparationTimeUseCase,
    private val getRandomMealUseCase: GetRandomMealUseCase,
    private val inputReader: InputReader,
    private val printer: OutputPrinter
) {
    fun playGuessGame() {
        getRandomMealUseCase.getRandomMeal().fold(
            onSuccess = ::startGame,
            onFailure = { printer.printLine(Constants.UNEXPECTED_ERROR) }
        )
    }

    private fun startGame(meal: Meal) {
        meal.minutes?.let { correctTime ->
            printer.printLine("🎮 Guess the preparation time of: ${meal.name}")

            (1..MAX_ATTEMPTS).forEach { attempt ->
                printer.printLine("Enter your guess:")
                val userGuess = inputReader.readIntOrNull()

                userGuess
                    ?.also { handleGuessGame(it, correctTime) }
                    ?: run {
                        printer.printLine(Constants.INVALID_INPUT_MESSAGE)
                        return
                    }

                if (attempt == MAX_ATTEMPTS) {
                    printer.printLine("❌ You've used all attempts. The correct time was $correctTime minutes.")
                }
            }
        } ?: printer.printLine(Constants.NO_PREPARATION_TIME)
    }


    private fun handleGuessGame(userGuess: Int?, correctTime: Int?) {
        getGuessPreparationTimeUseCase.guessGame(
            userGuess = userGuess,
            preparationTime = correctTime
        ).map { state ->
            when (state) {
                GuessPreparationTimeState.CORRECT -> "🎉 Correct! The preparation time is $correctTime minutes."
                GuessPreparationTimeState.TOO_LOW -> Constants.LOW_MESSAGE
                GuessPreparationTimeState.TOO_HIGH -> Constants.HIGH_MESSAGE
                else -> "❗ Unexpected state."
            }
        }.onSuccess { message ->
            printer.printLine(message)
        }.onFailure {
            printer.printLine("❗ Failed to evaluate guess: ${it.message}")
        }
    }


    companion object {
        const val MAX_ATTEMPTS = 3
    }
}






