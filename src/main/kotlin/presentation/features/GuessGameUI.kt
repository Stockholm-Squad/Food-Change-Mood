package org.example.presentation.features

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
                val correctTime = meal.minutes
                if (correctTime == null) {
                    printer.printLine("❌ This meal has no preparation time.")
                    return
                }

                printer.printLine("🎮 Guess the preparation time of: ${meal.name}")
                var attempts = 0
                while (attempts < MAX_ATTEMPTS) {
                    printer.printLine("Enter your guess:")
                    val userGuess = inputReader.readLineOrNull()?.toIntOrNull()

                    if (userGuess == null) {
                        printer.printLine("❗ Invalid input. Please enter a number.")
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
                        GuessPreparationTimeState.TOO_LOW -> printer.printLine("⬇️ Too low.")
                        GuessPreparationTimeState.TOO_HIGH -> printer.printLine("⬆️ Too high.")
                        else -> printer.printLine("❗ Unexpected state.")
                    }

                    attempts++
                    if (attempts == MAX_ATTEMPTS) {
                        printer.printLine("❌ You've used all attempts. The correct time was $correctTime minutes.")
                        return
                    }
                }
            },
            onFailure = {
                printer.printLine(Constants.UNEXPECTED_ERROR)
            }
        )
    }
   companion object
   {
       const val MAX_ATTEMPTS=3
   }
}






