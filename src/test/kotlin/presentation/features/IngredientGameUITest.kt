package presentation.features

import com.google.common.truth.Truth
import io.mockk.*
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetIngredientGameUseCase
import org.example.logic.usecases.model.IngredientQuestionModel
import org.example.presentation.features.IngredientGameUI
import org.example.utils.Constants
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
      verify { printer.printLine(match { it.contains(Constants.WINNING_MESSAGE) }) }
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
      verify(atLeast = 1) { printer.printLine(match { it.contains(Constants.CORRECT_CHOICE) }) }
      verify(exactly = 0) { printer.printLine(match { it.contains(Constants.INCORRECT_MESSAGE) }) }
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
      verify(atLeast = 1) { printer.printLine(match { it.contains(Constants.INCORRECT_MESSAGE) }) }
      verify(exactly = 0) { printer.printLine(match { it.contains(Constants.CORRECT_CHOICE) }) }
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
      verify(exactly = 2) { printer.printLine(match { it.contains(Constants.INVALID_INPUT) }) }
      verify { printer.printLine(match { it.contains(Constants.INCORRECT_MESSAGE) }) }
   }

   @Test
   fun `start() should exit and print invalid input when user enters q to quit`() {
      val question = IngredientQuestionModel("Sushi", listOf("Rice", "Fish", "Seaweed"), "Fish")
      every { game.startIngredientGame() } returns Result.success(question)
      every { reader.readLineOrNull() } returns "q"

      ui.start()

      verify { printer.printLine(Constants.INVALID_INPUT) }
   }

   @Test
   fun `start() should print unexpected error when questionResult is failure`() {
      every { game.startIngredientGame() } returns Result.failure(Exception("Error"))
      ui.start()
      verify { printer.printLine(Constants.UNEXPECTED_ERROR) }
   }

   @Test
   fun `start() should display the question with all options`() {
      val question = IngredientQuestionModel("Pasta", listOf("Cheese", "Tomato", "Olives"), "Tomato")
      every { game.startIngredientGame() } returns Result.success(question)
      every { reader.readLineOrNull() } returns "2"
      every { game.submitAnswer("Tomato") } returns true

      ui.start()

      verify { printer.printLine(match { it.contains(question.mealName) }) }
      question.options.forEach { option ->
         verify { printer.printLine(match { string -> string.contains(option) }) }
      }
      verify { printer.printLine(match { it.contains(question.correctIngredient) }) }
   }

   @Test
   fun `start() should print the game introduction`() {
      val question = IngredientQuestionModel("Stew", listOf("Carrot", "Meat", "Potato"), "Carrot")
      every { game.startIngredientGame() } returns Result.success(question)
      every { reader.readLineOrNull() } returns "1"
      every { game.submitAnswer(question.correctIngredient) } returns false

      ui.start()

      verify { printer.printLine(Constants.WELCOME_INGREDIENTS_GAME_MESSAGE) }
   }

   @Test
   fun `start() should retry on empty input or spaces`() {
      //given
      val question = IngredientQuestionModel("Pizza", listOf("Cheese", "Tomato", "Olives"), "Tomato")
      every { game.startIngredientGame() } returns Result.success(question)
      every { reader.readLineOrNull() } returnsMany listOf(" ", "", "1")
      every { game.submitAnswer("Tomato") } returns true

      //when
      ui.start()

      //then
      verify(exactly = 2) { printer.printLine(match { it.contains(Constants.INVALID_INPUT) }) }
   }






}

