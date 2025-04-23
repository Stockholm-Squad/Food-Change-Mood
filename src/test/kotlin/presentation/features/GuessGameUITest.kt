package presentation.features

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.usecases.createMeal
import org.example.logic.usecases.GetGuessPreparationTimeUseCase
import org.example.logic.usecases.GetRandomMealUseCase
import org.example.logic.usecases.model.GuessPreparationTimeState
import org.example.presentation.features.GuessGameUI
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GuessGameUITest {
    private lateinit var getRandomMealUseCase: GetRandomMealUseCase
    private lateinit var getGuessPreparationTimeUseCase: GetGuessPreparationTimeUseCase
    private lateinit var guessGameUI: GuessGameUI


    @BeforeEach
    fun setUp() {
        getRandomMealUseCase = mockk(relaxed = true)
        getGuessPreparationTimeUseCase = mockk(relaxed = true)
        guessGameUI = GuessGameUI(getGuessPreparationTimeUseCase, getRandomMealUseCase)
    }


    @Test
    fun `playGuessGame should print correct message when user guesses correctly`() {
        //Given
        val meal = createMeal("aww  marinated olives", 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every {
            getGuessPreparationTimeUseCase.guessGame(
                userGuess = any(),
                attempts = any(),
                preparationTime = any()
            )
        } returns Result.success(
            GuessPreparationTimeState.CORRECT
        )
        //When
        val result = mutableListOf<String>()
        val guessGameUI = GuessGameUI(
            getGuessPreparationTimeUseCase,
            getRandomMealUseCase,
            inputProvider = { "15" },
            outputCollector = { result.add(it) }
        )
        guessGameUI.playGuessGame()
        //Then
        assertThat(result).contains("🎉 Correct! The preparation time is 15 minutes.")
    }

    @Test
    fun `playGuessGame should print too low message when user guesses low preparationTime `() {
        //Give
        val meal = createMeal(mealName = "aww  marinated olives", mealPreparationTime = 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every {
            getGuessPreparationTimeUseCase.guessGame(
                any(),
                any(),
                any()
            )
        } returns Result.success(GuessPreparationTimeState.TOO_LOW)
        //When
        val result = mutableListOf<String>()
        val guessGameUI = GuessGameUI(
            getGuessPreparationTimeUseCase,
            getRandomMealUseCase,
            inputProvider = { "10" },
            outputCollector = { result.add(it) }
        )
        guessGameUI.playGuessGame()
        //Then
        assertThat(result).contains("⬇️ Too low.")

    }

    @Test
    fun `playGuessGame should print too high when user guesses high preparationTime`() {
        //Given
        val meal = createMeal("aww  marinated olives", 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every {
            getGuessPreparationTimeUseCase.guessGame(
                any(),
                any(),
                any()
            )
        } returns Result.success(GuessPreparationTimeState.TOO_HIGH)
        //When
        val result = mutableListOf<String>()
        val guessGameUI = GuessGameUI(
            getGuessPreparationTimeUseCase,
            getRandomMealUseCase,
            { "30" },
            { result.add(it) }
        )
        guessGameUI.playGuessGame()
        //Then
        assertThat(result).contains("⬆️ Too high.")
    }

    @Test
    fun `playGuessGame should print correct time when user used all attempts`() {
        // Given
        val meal = createMeal("aww  marinated olives", 15)
        every { getRandomMealUseCase.getRandomMeal() } returns Result.success(meal)
        every {
            getGuessPreparationTimeUseCase.guessGame(any(), any(), any())
        } returnsMany listOf(
            Result.success(GuessPreparationTimeState.TOO_HIGH),
            Result.success(GuessPreparationTimeState.TOO_HIGH),
            Result.success(GuessPreparationTimeState.FAILED)
        )

        val result = mutableListOf<String>()
        val guessGameUI = GuessGameUI(
            getGuessPreparationTimeUseCase,
            getRandomMealUseCase,
            { "30" },
            { result.add(it) }
        )

        // When
        guessGameUI.playGuessGame()

        // Then
        assertThat(result).contains("❌ You've used all attempts. The correct time was 15 minutes.")
    }
    @Test
    fun `playGuessGame should print error message when getRandomMealUseCase fails`() {
        // Given
        val errorMessage = "Network error"
        every { getRandomMealUseCase.getRandomMeal() } returns Result.failure(Exception(errorMessage))

        val result = mutableListOf<String>()
        val guessGameUI = GuessGameUI(
            getGuessPreparationTimeUseCase = getGuessPreparationTimeUseCase,
            getRandomMealUseCase = getRandomMealUseCase,
            inputProvider = { "15" },
            outputCollector = { result.add(it) }
        )

        // When
        guessGameUI.playGuessGame()

        // Then
        assertThat(result).contains("Error: $errorMessage")
    }


}