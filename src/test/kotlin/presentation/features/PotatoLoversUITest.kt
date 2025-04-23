package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecases.createMeal
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.presentation.features.PotatoLoversUI
import org.example.utils.InputHandler
import org.example.utils.OutputHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PotatoLoversUITest{

 private lateinit var potatoMeals: GetPotatoMealsUseCase
 private lateinit var potatoMealUi: PotatoLoversUI
 private lateinit var outputHandler: OutputHandler
 private lateinit var inputHandler: InputHandler


 @BeforeEach
 fun setUp() {
  potatoMeals = mockk(relaxed = true)
  outputHandler = mockk(relaxed = true)
  inputHandler = mockk()
  potatoMealUi = PotatoLoversUI(
   potatoMeals,
   outputHandler,
   inputHandler
  )
 }

 @Test
 fun `showPotatoLoversUI should display meals when potato meal use case returns meals`() {
  // Given
  val meals = listOf(
   createMeal(1, "Mashed Potatoes", listOf("Potato", "Butter")),
   createMeal(2, "Potato Salad", listOf("Potato", "Onion"))
  )

  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputHandler.readInput() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify {
   outputHandler.showMessage("🥔 I ❤️ Potato Meals:")
   outputHandler.showMessage("🍽️ Meal #1: Mashed Potatoes")
   outputHandler.showMessage("🍽️ Meal #2: Potato Salad")
   outputHandler.showMessage(withArg { message ->
    assert(message.contains("Would you like to see more"))
   })
   outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋")
  }
 }

 @Test
 fun `should show no meals found when potato meal use case returns empty list`() {

  // Given
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(emptyList())
  every { inputHandler.readInput() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("😢 No potato meals found.") }
 }

 @Test
 fun `should show error when potato meal use case fails`() {

  // Given
  val exception = RuntimeException("Something went wrong")
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.failure(exception)
  every { inputHandler.readInput() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  //Then
  verify { outputHandler.showMessage("❌ Error: ${exception.message}") }
 }

 @Test
 fun `should not crash when inputHandler is null`() {
  // Given
  val potatoUi = PotatoLoversUI(potatoMeals, outputHandler, null)
  val meals = listOf(createMeal(1, "Chips", listOf("Potato")))

  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)

  // When
  potatoUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("🍽️ Meal #1: Chips") }
 }

 @Test
 fun `should handle unexpected input as no`() {

  //Given
  val meals = listOf(createMeal(1, "Hash Browns", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputHandler.readInput() } returns "maybe"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `should recall showPotatoLoversUI when input is capital Y`() {

  // Given
  val meals = listOf(createMeal(1, "Baked Potato", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputHandler.readInput() } returnsMany listOf("Y", "n")

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify(exactly = 2) { potatoMeals.getRandomPotatoMeals(10) }
  verify { outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `should recall showPotatoLoversUI when input is capital N`() {

  // Given
  val meals = listOf(createMeal(1, "Baked Potato", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputHandler.readInput() } returnsMany listOf("y", "N")

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify(exactly = 2) { potatoMeals.getRandomPotatoMeals(10) }
  verify { outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `should display one meal correctly`() {

  // Given
  val meals = listOf(createMeal(1, "Potato Gratin", listOf("Potato", "Cheese")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputHandler.readInput() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify {
   outputHandler.showMessage("🍽️ Meal #1: Potato Gratin")
  }
 }

 @Test
 fun `should handle meal with empty name`() {

  // Given
  val meals = listOf(createMeal(1, "", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputHandler.readInput() } returns "n"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("🍽️ Meal #1: ") }
 }

 @Test
 fun `should treat null or blank input as no`() {

  // Given
  val meals = listOf(createMeal(1, "Potato Soup", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(10) } returns Result.success(meals)
  every { inputHandler.readInput() } returnsMany listOf("   ", null)

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `should show no meals found when result is null`() {
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(emptyList())
  every { inputHandler.readInput() } returns "n"

  potatoMealUi.showPotatoLoversUI()

  verify { outputHandler.showMessage("😢 No potato meals found.") }
 }

 @Test
 fun `should show error message when result is failure`() {

  // Given
  val exception = RuntimeException("Network error")
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.failure(exception)

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("❌ Error: ${exception.message}") }
 }

 @Test
 fun `should fetch and show more meals when user inputs y then stop on n`() {

  // Given
  val meals = (1..2).map { createMeal(it, "Meal $it", listOf("Potato")) }

  every { potatoMeals.getRandomPotatoMeals(10) } returnsMany listOf(
   Result.success(meals),
   Result.success(meals)
  )
  every { inputHandler.readInput() } returnsMany listOf("y", "n")

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify(exactly = 2) { potatoMeals.getRandomPotatoMeals(10) }
  verify(exactly = 2) { outputHandler.showMessage("🥔 I ❤️ Potato Meals:") }

  meals.forEachIndexed { index, meal ->
   verify(exactly = 2) { outputHandler.showMessage("🍽️ Meal #${index + 1}: ${meal.name}") }
  }

  verify { outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `should treat unknown answer as no`() {

  // Given
  val meals = listOf(createMeal(1, "Fries", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(meals)
  every { inputHandler.readInput() } returns "hello"

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `should handle null input `() {

  // Given
  val meals = listOf(createMeal(1, "Potato Salad", listOf("Potato")))
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(meals)
  every { inputHandler.readInput() } returns null

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("Okay! Enjoy your potato meals! 🥔😋") }
 }

 @Test
 fun `should show no meals message when result is success but meals are empty`() {

  // Given
  every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(emptyList())

  // When
  potatoMealUi.showPotatoLoversUI()

  // Then
  verify { outputHandler.showMessage("😢 No potato meals found.") }
 }


}