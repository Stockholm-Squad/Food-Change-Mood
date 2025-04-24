package presentation.features

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import logic.usecases.buildMeal
import logic.usecases.createMeal
import model.Nutrition
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
 fun `showPotatoLoversUI should handle when inputHandler read a readInput is empty string`() {
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
  verify(exactly = 1) { potatoMeals.getRandomPotatoMeals(10) }
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
  verify(exactly = 1) { potatoMeals.getRandomPotatoMeals(10) }
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
 fun `showPotatoLoversUI should handle when input is a number`() {

  // Given
  val meals = listOf(createMeal(1, "Potato Wedges", listOf("Potato")))
  val inputCount = 10
  every { potatoMeals.getRandomPotatoMeals(inputCount) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returns "1"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
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

 @Test
 fun `showPotatoLoversUI should handle null input from user`() {

  // Given
  val meals = listOf(createMeal(1, "Potato Bake", listOf("Potato")))
  val inputCount = 10
  every { potatoMeals.getRandomPotatoMeals(inputCount) } returns Result.success(meals)
  every { inputReader.readLineOrNull() } returns null

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `askToViewMealDetails should prompt the user with the initial question`() {
  // Given
  val meals = listOf(buildMeal(id = 1, name = "Potato Casserole", ingredients = listOf("Potato", "Cheese"), steps = listOf("Boil potatoes", "Add cheese", "Bake in oven"), minutes = 30, numberOfSteps = 3, numberOfIngredients = 2, description = "A cheesy baked potato dish", nutrition = Nutrition(null, null, null, null, null, null, null), submitted = null, contributorId = null),
   buildMeal(id = 1, name = "Potato Casserole", ingredients = listOf("Potato", "Cheese"), steps = listOf("Boil potatoes", "Add cheese", "Bake in oven"), minutes = 30, numberOfSteps = 3, numberOfIngredients = 2, description = "A cheesy baked potato dish", nutrition = Nutrition(null, null, null, null, null, null, null), submitted = null, contributorId = null))

  // When
  every { inputReader.readLineOrNull() } returns "n"

  // Then
  potatoMealUi.askToViewMealDetails(meals)

  verify { outputPrinter.printLine("\nWould you like to view the details of any of these meals? (Enter the number or 'n' to skip):") }
 }

 @Test
 fun `askToViewMealDetails should skip meal details when user enters 'n'`() {
  // Given
  val meals = listOf(buildMeal(id = 1, name = "Potato Casserole", ingredients = listOf("Potato", "Cheese"), steps = listOf("Boil potatoes", "Add cheese", "Bake in oven"), minutes = 30, numberOfSteps = 3, numberOfIngredients = 2, description = "A cheesy baked potato dish", nutrition = Nutrition(null, null, null, null, null, null, null), submitted = null, contributorId = null),
   buildMeal(id = 1, name = "Potato Casserole", ingredients = listOf("Potato", "Cheese"), steps = listOf("Boil potatoes", "Add cheese", "Bake in oven"), minutes = 30, numberOfSteps = 3, numberOfIngredients = 2, description = "A cheesy baked potato dish", nutrition = Nutrition(null, null, null, null, null, null, null), submitted = null, contributorId = null))

  // When
  every { inputReader.readLineOrNull() } returns "n"

  // Then
  potatoMealUi.askToViewMealDetails(meals)

  verify {
   outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋")
  }
 }

 @Test
 fun `askToViewMealDetails should ask again if input is invalid (non-numeric or empty)`() {
  // Given
  val meals = listOf(buildMeal(id = 1, name = "Potato Casserole", ingredients = listOf("Potato", "Cheese"), steps = listOf("Boil potatoes", "Add cheese", "Bake in oven"), minutes = 30, numberOfSteps = 3, numberOfIngredients = 2, description = "A cheesy baked potato dish", nutrition = Nutrition(null, null, null, null, null, null, null), submitted = null, contributorId = null),
   buildMeal(id = 1, name = "Potato Casserole", ingredients = listOf("Potato", "Cheese"), steps = listOf("Boil potatoes", "Add cheese", "Bake in oven"), minutes = 30, numberOfSteps = 3, numberOfIngredients = 2, description = "A cheesy baked potato dish", nutrition = Nutrition(null, null, null, null, null, null, null), submitted = null, contributorId = null))

  // When
  every { inputReader.readLineOrNull() } returns "" andThen "abc" andThen "1"

  // Then
  potatoMealUi.askToViewMealDetails(meals)

  verify {
   outputPrinter.printLine("Invalid selection. Please choose a valid number.")
   outputPrinter.printLine("Invalid selection. Please choose a valid number.")
  }
 }

 @Test
 fun `askToViewMealDetails should show invalid selection message when index is out of range`() {
  // Given
  val meals = List(10) { index ->
   buildMeal(
    id = index + 1,
    name = "Meal #${index + 1}",
    ingredients = listOf("Ingredient A", "Ingredient B"),
    steps = listOf("Step 1", "Step 2"),
    minutes = 20,
    numberOfSteps = 2,
    numberOfIngredients = 2,
    description = "Test meal",
    nutrition = Nutrition(null,
     null,
     null,
     null,
     null,
     null,
     null),
    submitted = null,
    contributorId = null
   )
  }

  // When
  every { inputReader.readLineOrNull() } returnsMany listOf("11", "n")

  // Then
  potatoMealUi.askToViewMealDetails(meals)

  verify {
   outputPrinter.printLine("Invalid selection. Please choose a valid number.")
   outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋")
  }
 }
 @Test
 fun `askIfWantsMore should call onYes callback when input is y`() {
  // Given
  every { inputReader.readLineOrNull() } returns "y"
  var called = false

  // When
  potatoMealUi.askIfWantsMore {
   called = true
  }

  // Then
  assertThat(called).isTrue()
 }

 @Test
 fun `askIfWantsMore should print end message when input is n`() {
  // Given
  every { inputReader.readLineOrNull() } returns "n"

  // When
  potatoMealUi.askIfWantsMore()

  // Then
  verify { outputPrinter.printLine("Okay! Enjoy your potato meals! 🥔😋") }
 }


}