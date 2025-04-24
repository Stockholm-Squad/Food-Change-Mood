package presentation.features

import io.mockk.mockk
import org.example.logic.usecases.GetIngredientGameUseCase
import org.example.logic.usecases.model.IngredientQuestionModel
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class IngredientGameUITest {
   private lateinit var game: GetIngredientGameUseCase
   private lateinit var question : IngredientQuestionModel

   @BeforeEach
   fun setUp() {
    game = mockk(relaxed = true)
   }
}