import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetIngredientGameUseCase
import org.example.logic.usecases.model.IngredientQuestionModel

class IngredientGameUI(
    private val game: GetIngredientGameUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter
) {
    private var score = 0
    private val pointsPerScore = 1000
    private val winningPoints = 15000


    fun start() {
        printIntro()

        while (true) {
            val result = handleAction()
            if (!result) break
        }
    }

    private fun printIntro() {
        printer.printLine("🧠 Ingredient Game is starting! Let's see how well you know your meals!")
        printer.printLine("Guess the correct ingredient for each meal.")
        printer.printLine("Get 15 right to win! One wrong answer ends the game.")
        printer.printLine("--------------------------------------------------")
    }

    private fun handleAction(): Boolean {
        val questionResult = game.startIngredientGame()

        return questionResult.fold(
            onSuccess = { question ->
                displayQuestion(question)
                val userChoice = handleInput(question)
                if (userChoice == null) {
                    printer.printLine("⚠️ Could not get valid input. Exiting game.")
                    return false
                }

                val selected = question.options[userChoice - 1]
                val isCorrect = game.submitAnswer(selected)
                handleResult(isCorrect)
            },
            onFailure = {
                printer.printLine("❌ Error loading question: ${it.message}")
                false
            }
        )
    }

    private fun handleInput(question: IngredientQuestionModel): Int? {
        while (true) {
            printer.printLine("Enter your choice (1-${question.options.size}): ")
            val input = reader.readLineOrNull()
            val choice = input?.toIntOrNull()
            if (choice != null && choice in 1..question.options.size) {
                return choice
            }
            printer.printLine("❗ Invalid input. Please enter a number between 1 and ${question.options.size}.")
        }
    }

    private fun handleResult(isCorrect: Boolean): Boolean {
        return if (isCorrect) {
            score += pointsPerScore
            printer.printLine("✅ Correct! Your current score: $score")
            printer.printLine("--------------------------------------------------")
            if (score >= winningPoints) {
                printer.printLine("\n🏁 Congratulations! You reached the winning score: $score")
                false
            } else {
                true
            }
        } else {
            printer.printLine("🏁 Game Over! Final Score: $score")
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
