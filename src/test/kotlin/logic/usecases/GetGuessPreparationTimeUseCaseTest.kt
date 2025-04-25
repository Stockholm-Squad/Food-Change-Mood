package logic.usecases

import com.google.common.truth.Truth.assertThat
import org.example.logic.usecases.GetGuessPreparationTimeUseCase
import org.example.logic.usecases.model.GuessPreparationTimeState
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetGuessPreparationTimeUseCaseTest {


    private lateinit var getGuessPreparationTimeUseCase: GetGuessPreparationTimeUseCase


    @BeforeEach
    fun setUp(){
       getGuessPreparationTimeUseCase= GetGuessPreparationTimeUseCase()
    }


    @Test
    fun `guessGame () should return correct when guess matches preparation time`() {
        //Given
        val userGuess = 15
        val preparationTime = 15
        //When
        val result = getGuessPreparationTimeUseCase.guessGame(userGuess , preparationTime )
        //Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.CORRECT)
    }
    @Test
    fun `guessGame () should return too high when guess greater than preparation time`() {
        //Given
        val userGuess = 30
        val preparationTime = 15
        //When
        val result = getGuessPreparationTimeUseCase.guessGame(userGuess , preparationTime )
        //Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.TOO_HIGH)
    }
    @Test
    fun `guessGame () should return too low when userGuess less than preparationTime`(){
        // Given
        val userGuess = 15
        val preparationTime = 30

        // When
        val result=getGuessPreparationTimeUseCase.guessGame(userGuess,preparationTime)
        //Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.TOO_LOW)

    }@Test
    fun `guessGame () should return failed when userGuess is null`() {
        // Given
        val userGuess: Int? = null
        val preparationTime: Int? = 20

        // When
        val result = getGuessPreparationTimeUseCase.guessGame(userGuess, preparationTime)

        // Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.FAILED)
    }

    @Test
    fun `guessGame () should return failed when preparationTime is null`() {
        // Given
        val userGuess: Int? = 10
        val preparationTime: Int? = null

        // When
        val result = getGuessPreparationTimeUseCase.guessGame(userGuess, preparationTime)

        // Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.FAILED)
    }

    @Test
    fun `guessGame() should return failed when all attempts have`() {
        // Given
        val userGuess: Int? = 10
        val preparationTime: Int? = null

        // When
        val result = getGuessPreparationTimeUseCase.guessGame(userGuess, preparationTime)

        // Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.FAILED)
    }

}