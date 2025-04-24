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
            onSuccess = { meal ->
                startGame(meal)
            },
            onFailure = {
                printer.printLine(Constants.UNEXPECTED_ERROR)
            }
        )
    }
    private fun startGame(meal: Meal){
        val correctTime = meal.minutes
        if (correctTime == null) {
            printer.printLine(Constants.NO_PREPARATION_TIME)
            return
        }

        printer.printLine("🎮 Guess the preparation time of: ${meal.name}")
        var attempts = 0
        while (attempts < MAX_ATTEMPTS) {
            printer.printLine("Enter your guess:")
            val userGuess = inputReader.readIntOrNull()

            if (userGuess == null) {
                printer.printLine(Constants.INVALID_INPUT_MESSAGE)
                continue
            }

            val result = getGuessPreparationTimeUseCase.guessGame(
                userGuess = userGuess,
                preparationTime = correctTime
            )

            when (result.getOrNull()) {
                GuessPreparationTimeState.CORRECT -> {
                    printer.printLine("🎉 Correct! The preparation time is $correctTime minutes.")
                    return
                }
                GuessPreparationTimeState.TOO_LOW -> printer.printLine(Constants.LOW_MESSAGE)
                GuessPreparationTimeState.TOO_HIGH -> printer.printLine(Constants.HIGH_MESSAGE)
                else -> printer.printLine("❗ Unexpected state.")
            }

            attempts++
            if (attempts == MAX_ATTEMPTS) {
                printer.printLine("❌ You've used all attempts. The correct time was $correctTime minutes.")
                return
            }
        }
    }
   companion object
   {
       const val MAX_ATTEMPTS=3
   }
}






