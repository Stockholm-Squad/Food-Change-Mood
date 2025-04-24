package presentation.features

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecases.createMeal
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.presentation.features.PotatoLoversUI
import org.example.utils.InputReader
import org.example.utils.OutputPrinter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

class PotatoLoversUITest {

 private lateinit var potatoMeals: GetPotatoMealsUseCase
 private lateinit var potatoMealUi: PotatoLoversUI
 private lateinit var outputPrinter: OutputPrinter
 private lateinit var inputReader: InputReader

 @BeforeEach
 fun setUp() {
  potatoMeals = mockk(relaxed = true)
  outputPrinter = mockk(relaxed = true)
  inputReader = mockk()
  potatoMealUi = PotatoLoversUI(
   potatoMeals,
   outputPrinter,
   inputReader
  )
 }

 @Test
 fun `showPotatoLoversUI should print meals when call GetPotatoMealUseCase with valid potato meals`() {

  // Given
  val meals = listOf(
   createMeal(1, "Mashed Potatoes", listOf("Potato", "Butter")),
   createMeal(2, "Potato Salad", listOf("Potato", "Onion"))
  )

  val inputCount = 10
  every { potatoMeals.getRandomPotatoMeals(inputCount) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify {
   outputPrinter.printLine("🥔 I ❤️ Potato Meals:")
   outputPrinter.printLine("🍽️ Meal #1: Mashed Potatoes")
   outputPrinter.printLine("🍽️ Meal #2: Potato Salad")
   outputPrinter.printLine(withArg { message ->
    assert(message.contains("Would you like to see more"))
   })
   outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋")
  }
 }

 @Test
 fun `handleSuccess should show potato meals when meals are found`() {
  // Given
  val meals = listOf(
   createMeal(1, "Mashed Potatoes", listOf("Potato", "Butter")),
   createMeal(2, "Potato Salad", listOf("Potato", "Onion"))
  )

  // When
  potatoMealUi.handleSuccess(meals)

  // Then
  verify { outputPrinter.printLine("🥔 I ❤️ Potato Meals:") }
  verify { outputPrinter.printLine("🍽️ Meal #1: Mashed Potatoes") }
  verify { outputPrinter.printLine("🍽️ Meal #2: Potato Salad") }
 }

 @Test
 fun `handleFailure should show error message when an exception occurs`() {
  // Given
  val exception = RuntimeException("Something went wrong")

  // When
  potatoMealUi.handleFailure(exception)

  // Then
  verify { outputPrinter.printLine("❌ Error: ${exception.message}") }
 }

 @Test
 fun `showPotatoLoversUI should show no meals found when GetPotatoMealUseCase returns empty list`() {
  // Given
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(emptyList())
  every { inputReader.readLineOrNull() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("😢 No potato meals found.") }
 }

 @Test
 fun `showPotatoLoversUI should show error when GetPotatoMealUseCase fails`() {

  // Given
  val exception = RuntimeException("Something went wrong")
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.failure(exception)
  every { inputReader.readLineOrNull() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("❌ Error: ${exception.message}") }
 }

 @Test
 fun `showPotatoLoversUI should not crash when inputHandler read a readInput is empty string`() {
  // Given
  val potatoUi = PotatoLoversUI(potatoMeals, outputPrinter, null)
  val meals = listOf(createMeal(1, "Chips", listOf("Potato")))

  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)

  // When
  potatoUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("🍽️ Meal #1: Chips") }
 }

 @Test
 fun `askForMoreMeals should recall showPotatoLoversUI when input is capital Y`() {

  // Given
  val meals = listOf(createMeal(1, "Baked Potato", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returnsMany listOf("Y", "n")

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify(exactly = 2) { potatoMeals.getRandomPotatoMeals(10) }
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `askForMoreMeals should not recall showPotatoLoversUI when input is capital N`() {

  // Given
  val meals = listOf(createMeal(1, "Baked Potato", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returnsMany listOf("y", "N")

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify(exactly = 2) { potatoMeals.getRandomPotatoMeals(10) }
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `askForMoreMeals should handle unexpected input as no`() {

  // Given
  val meals = listOf(createMeal(1, "Hash Browns", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returns "no"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `askForMoreMeals should stop when input is no`() {

  // Given
  val meals = listOf(createMeal(1, "Fries", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `showPotatoLoversUI should handle whitespace input as no`() {
  // Given
  val meals = listOf(createMeal(1, "Potato Pancakes", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returns "   "

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `showPotatoLoversUI should handle multiple consecutive yes responses`() {

  // Given
  val meals1 = listOf(createMeal(1, "First Meal", listOf("Potato")))
  val meals2 = listOf(createMeal(1, "Second Meal", listOf("Potato")))
  val meals3 = listOf(createMeal(1, "Third Meal", listOf("Potato")))


  val inputCount = 10
  every { potatoMeals.getRandomPotatoMeals(inputCount) } returnsMany
          listOf(Result.success(meals1), Result.success(meals2), Result.success(meals3))
  every { inputReader.readLineOrNull() } returnsMany listOf("y", "yes", "n")

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify(exactly = 2) { potatoMeals.getRandomPotatoMeals(inputCount) }
 }

 @Test
 fun `showPotatoLoversUI should handle edge case when input is a number`() {

  // Given
  val meals = listOf(createMeal(1, "Potato Wedges", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returns "1"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `showPotatoLoversUI should handle input failure `() {

  // Given
  val meals = listOf(createMeal(1, "Potato Gratin", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } throws IOException("Input error")

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("❌ Error: Input error") }
 }

 @Test
 fun `askForMoreMeals should treat mixed case input with spaces as yes`() {

  // Given
  val meals = listOf(createMeal(1, "Roasted Potatoes", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returnsMany listOf(
   Result.success(meals),
   Result.success(emptyList())
  )
  every { inputReader.readLineOrNull() } returnsMany listOf("  Y   ", "n")

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify(exactly = 2) { potatoMeals.getRandomPotatoMeals(10) }
 }

 @Test
 fun `normalizeInput trims and lowercases correctly`() {
  val result = PotatoLoversUI.normalizeInput("  Y ")
  assertThat(result).isEqualTo("y")
 }

 @Test
 fun `normalizeInput returns empty for null input`() {
  val result = PotatoLoversUI.normalizeInput(null)
  assertThat(result).isEmpty()
 }


}