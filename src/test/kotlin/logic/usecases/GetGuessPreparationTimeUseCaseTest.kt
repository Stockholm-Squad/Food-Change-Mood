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
    fun `guessGame should return CORRECT when guess matches preparation time`() {
        //Given
        val userGuess = 15
        val attempts = 0
        val preparationTime = 15
        //When
        val result = getGuessPreparationTimeUseCase.guessGame(userGuess, attempts , preparationTime )
        //Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.CORRECT)
    }
    @Test
    fun `guessGame should return TOO_HIGH when guess greater than preparation time`() {
        //Given
        val userGuess = 30
        val attempts = 0
        val preparationTime = 15
        //When
        val result = getGuessPreparationTimeUseCase.guessGame(userGuess, attempts , preparationTime )
        //Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.TOO_HIGH)
    }
    @Test
    fun `guessGame should return TOO_LOW when userGuess less than preparationTime`(){
        // Given
        val userGuess = 15
        val attempts = 0
        val preparationTime = 30

        // When
        val result=getGuessPreparationTimeUseCase.guessGame(userGuess,attempts,preparationTime)
        //Then
        assertThat(result.getOrNull()).isEqualTo(GuessPreparationTimeState.TOO_LOW)

    }
}