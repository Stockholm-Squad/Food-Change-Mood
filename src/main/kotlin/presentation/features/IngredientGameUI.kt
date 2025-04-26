package org.example.presentation.features


import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetIngredientGameUseCase
import org.example.logic.usecases.model.IngredientQuestionModel
import org.example.utils.Constants

class IngredientGameUI(
    private val game: GetIngredientGameUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter
) {
    private var score = 0
    private val pointsPerScore = 1000
    private val winningPoints = 15000


    fun start() {
        printer.printLine(Constants.WELCOME_INGREDIENTS_GAME_MESSAGE)

        while (true) {
            val result = handleAction()
            if (!result) break
        }
    }

    private fun handleAction(): Boolean {
        val questionResult = game.startIngredientGame()

        return questionResult.fold(
            onSuccess = { question ->
                displayQuestion(question)
                handleInput(question)
                    ?.takeIf { it in 1..question.options.size }
                    ?.let { question.options[it - 1] }
                    ?.let { selected ->
                        game.submitAnswer(selected)
                    }
                    ?.let { isCorrect ->
                        handleResult(isCorrect)
                    }
                    ?: run {
                        printer.printLine(Constants.INVALID_INPUT)
                        false
                    }
            },
            onFailure = {
                printer.printLine(Constants.UNEXPECTED_ERROR)
                false
            }
        )
    }

    private fun handleInput(question: IngredientQuestionModel): Int? {
        while (true) {
            printer.printLine("Enter your choice (1-${question.options.size}) or 'q' to quit: ")
            val input = reader.readStringOrNull()
            if (input == "q") return null
            val choice = input?.toIntOrNull()
            if (choice != null && choice in 1..question.options.size) {
                return choice
            }
            printer.printLine(Constants.INVALID_INPUT)
        }
    }

    private fun handleResult(isCorrect: Boolean): Boolean {
        return if (isCorrect) {
            score += pointsPerScore
            printer.printLine(Constants.CORRECT_CHOICE + "$score")
            if (score >= winningPoints) {
                printer.printLine(Constants.WINNING_MESSAGE + score)
                false
            } else {
                true
            }
        } else {
            printer.printLine(Constants.INCORRECT_MESSAGE + "$score")
            false
        }
    }

    private fun displayQuestion(question: IngredientQuestionModel) {
        printer.printLine("\nMeal: ${question.mealName}")
        printer.printLine("Choose the correct ingredient:")
        question.options.forEachIndexed { index, option ->
            printer.printLine("${index + 1}. $option")
        }
    }
}
