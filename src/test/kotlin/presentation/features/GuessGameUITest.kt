package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetGuessPreparationTimeUseCase
import org.example.logic.usecases.GetRandomMealUseCase
import org.example.logic.usecases.model.GuessPreparationTimeState
import org.example.presentation.features.GuessGameUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import kotlin.test.Test

class GuessGameUITest {
    private lateinit var getRandomMealUseCase: GetRandomMealUseCase
    private lateinit var getGuessPreparationTimeUseCase: GetGuessPreparationTimeUseCase
    private lateinit var guessGameUI: GuessGameUI
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        getRandomMealUseCase = mockk(relaxed = true)
        getGuessPreparationTimeUseCase = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        guessGameUI = GuessGameUI(getGuessPreparationTimeUseCase, getRandomMealUseCase, reader, printer)
    }


    @Test
    fun `playGuessGame () should print correct message when user guesses correctly`() {
        //Given
        val meal = buildMeal(1, "aww  marinated olives", 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every { reader.readLineOrNull() } returns "15"
        every {
            getGuessPreparationTimeUseCase.guessGame(
                userGuess = any(),
                preparationTime = any()
            )
        } returns Result.success(
            GuessPreparationTimeState.CORRECT
        )
        //When
        guessGameUI.playGuessGame()
        //Then
        verify { printer.printLine("🎉 Correct! The preparation time is 15 minutes.") }
    }

    @Test
    fun `playGuessGame () should print too low message when user guesses low preparationTime `() {
        //Give
        val meal = buildMeal(2, "aww  marinated olives", 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every { reader.readLineOrNull() } returnsMany listOf("10", "10", "10")
        every {
            getGuessPreparationTimeUseCase.guessGame(
                any(),
                any()
            )
        } returns Result.success(GuessPreparationTimeState.TOO_LOW)
        //When
        guessGameUI.playGuessGame()
        //Then
        verify { printer.printLine("⬇️ Too low.") }

    }

    @Test
    fun `playGuessGame () should print too high when user guesses high preparationTime`() {
        // Given
        val meal = buildMeal(3, "aww marinated olives", 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every { reader.readLineOrNull() } returnsMany listOf("20", "20", "20")
        every {
            getGuessPreparationTimeUseCase.guessGame(any(), any())
        } returns Result.success(GuessPreparationTimeState.TOO_HIGH)

        // When
        guessGameUI.playGuessGame()

        // Then
        verify {
            printer.printLine("⬆️ Too high.")
        }
    }


    @Test
    fun `playGuessGame () should print correct time when user used all attempts`() {
        // Given
        val meal = buildMeal(4, "aww marinated olives", 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every { reader.readLineOrNull() } returnsMany listOf("20", "20","20")
        every {
            getGuessPreparationTimeUseCase.guessGame(any(), any())
        } returnsMany listOf(
            Result.success(GuessPreparationTimeState.TOO_HIGH),
            Result.success(GuessPreparationTimeState.TOO_HIGH),
            Result.success(GuessPreparationTimeState.FAILED)
        )

        // When
        guessGameUI.playGuessGame()

        // Then
        verify { printer.printLine("❌ You've used all attempts. The correct time was 15 minutes.") }
    }

    @Test
    fun `playGuessGame () should print error message when getRandomMealUseCase fails`() {
        // Given
        every { getRandomMealUseCase.getRandomMeal() } returns Result.failure(Exception())
        // When
        guessGameUI.playGuessGame()

        // Then
        verify { printer.printLine(Constants.UNEXPECTED_ERROR) }
    }


    @Test
    fun `playGuessGame () should print invalid input when user inputs non-numeric value`() {
        //Given
        val meal = buildMeal(5, "aww marinated olives", 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every { reader.readLineOrNull() } returnsMany listOf( "?","a","n","15")
        every {
            getGuessPreparationTimeUseCase.guessGame(
                userGuess = any(),
                preparationTime = any()
            )
        } returns Result.success(
            GuessPreparationTimeState.CORRECT
        )
        //When
        guessGameUI.playGuessGame()
        //Then
        verify { printer.printLine("❗ Invalid input. Please enter a number.") }
        verify { printer.printLine("🎉 Correct! The preparation time is 15 minutes.") }

    }
    @Test
    fun `playGuessGame () should print error message when meal has no preparation time`() {
        //Given
        val meal = buildMeal(5, "aww marinated olives", null)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every { reader.readLineOrNull() } returns  "20"
        //When
        guessGameUI.playGuessGame()
        //Then
        verify { printer.printLine("❌ This meal has no preparation time.") }

    }
    @Test
    fun `playGuessGame () should return failed when userGuess is null`() {
        // Given
        val meal = buildMeal(1, "aww marinated olives", 30)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every { reader.readLineOrNull() } returns null andThen "30"
        every {
            getGuessPreparationTimeUseCase.guessGame(
                userGuess = any(),
                preparationTime = any()
            )
        } returns Result.success(
            GuessPreparationTimeState.CORRECT
        )

        // When
        guessGameUI.playGuessGame()

        // Then
        verify { printer.printLine("❗ Invalid input. Please enter a number.") }
        verify { printer.printLine("🎉 Correct! The preparation time is 30 minutes.") }

    }
}