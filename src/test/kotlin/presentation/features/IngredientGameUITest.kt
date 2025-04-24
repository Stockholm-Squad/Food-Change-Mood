package presentation.features

import IngredientGameUI
import io.mockk.*
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetIngredientGameUseCase
import org.example.logic.usecases.model.IngredientQuestionModel
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class IngredientGameUITest {
   private lateinit var game: GetIngredientGameUseCase
   private lateinit var reader: InputReader
   private lateinit var printer: OutputPrinter
   private lateinit var ui: IngredientGameUI

   @BeforeEach
   fun setUp() {
      game = mockk(relaxed = true)
      reader = mockk(relaxed = true)
      printer = mockk(relaxed = true)
      ui = IngredientGameUI(game, reader, printer)
   }


   @Test
   fun `start() should print Congratulations after 15 correct answers - should win the game`() {
      //given
      val question = IngredientQuestionModel("Pasta", listOf("Tomato", "Cheese", "Basil"), "Tomato")
      every { game.startIngredientGame() } returns Result.success(question)
      every { reader.readLineOrNull() } returns "1"
      every { game.submitAnswer("Tomato") } returns true

      //when
      ui.start()

      //then
      verify(exactly = 15) { game.submitAnswer("Tomato") }
      verify { printer.printLine(match { it.contains("🏁 Congratulations") }) }
   }


   @Test
   fun `start() should print correct after select correct ingredient`() {
      val question = IngredientQuestionModel("Pizza", listOf("Cheese", "Tomato", "Olives") , "Lettuce")
      every { game.startIngredientGame() } returns Result.success(question)
      every { reader.readLineOrNull() } returns "1"
      every { game.submitAnswer("Cheese") } returns true

      //when
      ui.start()

      //then
      verify(atLeast = 1) { printer.printLine(match { it.contains("✅ Correct") }) }
      verify(exactly = 0) { printer.printLine(match { it.contains("🏁 Game Over") }) }
   }

   @Test
   fun `start() should print Game Over after select incorrect ingredient`() {
      //given
      val question = IngredientQuestionModel("Burger", listOf("Lettuce", "Pickles", "Beef"), "Lettuce")
      every { game.startIngredientGame() } returns Result.success(question)
      every { reader.readLineOrNull() } returns "2"
      every { game.submitAnswer("Pickles") } returns false
      //when
      ui.start()
      //then
      verify(atLeast = 1) { printer.printLine(match { it.contains("🏁 Game Over") }) }
      verify(exactly = 0) { printer.printLine(match { it.contains("✅ Correct") }) }
   }

   @Test
   fun `getUserChoice() invalid then valid input - should retry until valid`() {
      //given
      val question = IngredientQuestionModel("Salad", listOf("Lettuce", "Cucumber", "Tomato"), "Lettuce")
      every { game.startIngredientGame() } returns Result.success(question)
      every { reader.readLineOrNull() } returnsMany listOf("0", "abc", "2")
      every { game.submitAnswer("Cucumber") } returns false
      //when
      ui.start()
      //then
      verify(exactly = 2) { printer.printLine(match { it.contains("❗ Invalid input") }) }
      verify { printer.printLine(match { it.contains("🏁 Game Over") }) }
   }
}
