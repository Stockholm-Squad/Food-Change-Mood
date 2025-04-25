

package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import model.Meal
import com.google.common.truth.Truth.assertThat
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.repository.MealsRepository

import org.example.logic.usecases.GetMealForSoThinPeopleUseCase
import org.example.model.FoodChangeMoodExceptions.LogicException.NoMealsForSoThinPeopleException
import org.example.presentation.features.SuggestMealForSoThinPeopleUI
import org.example.utils.Constants.NO_MEAL_FOR_SO_THIN_PEOPLE
import org.example.utils.Constants.INVALID_INPUT
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.buildMeal
import utils.buildNutrition

class SuggestMealForSoThinPeopleUITest {
 private lateinit var suggestMealForSoThinPeopleUI: SuggestMealForSoThinPeopleUI
 private lateinit var getMealForSoThinPeople: GetMealForSoThinPeopleUseCase
 private lateinit var reader: InputReader
 private lateinit var printer: OutputPrinter
 private lateinit var meal: Meal

 @BeforeEach
 fun setup() {
  reader = mockk(relaxed = true)
  meal = mockk(relaxed = true)
  printer = mockk(relaxed = true)
  getMealForSoThinPeople = mockk(relaxed = true)
  suggestMealForSoThinPeopleUI = SuggestMealForSoThinPeopleUI(getMealForSoThinPeople, reader, printer)
 }

 @Test
 fun `showMealWithHighCalorie() should show meal details when user enter yes`() {
  val meal = buildMeal(1, nutrition = buildNutrition(calories = 750f))
  every { getMealForSoThinPeople.suggestRandomMealForSoThinPeople() } returns Result.success(meal)
  every { reader.readStringOrNull() } returns "yes"

  suggestMealForSoThinPeopleUI.showMealWithHighCalorie()

  verify {
   printer.printLine("-------------------------------------------------------------------------------------------------")
   printer.printLine("🔥 Feeling too thin? This meal above 700 calories!")
   printer.printLine("------------------------------------------------------------------------------------------------")
   printer.printLine("Name: ${meal.name}")
   printer.printLine("Description: ${meal.description}")
   printer.printLine("--------------------------------------------------------------------------------------------------")
   printer.printLine("Do you like it? (yes/no) 😊")
   printer.printLine("Meal Name: ${meal.name}")
   printer.printLine("Meal Description: ${meal.description}")
   printer.printLine("Meal Preparation Time: ${meal.minutes} minutes")
   printer.printLine("Meal ${meal.nutrition}")
  }
 }

 @Test
 fun `showMealWithHighCalorie() should print invalid input message when user gives unknown input`() {
  val meal = buildMeal(1, nutrition = buildNutrition(calories = 750f))
  every { getMealForSoThinPeople.suggestRandomMealForSoThinPeople() } returns Result.success(meal)
  every { reader.readStringOrNull() } returns " NaN "

  suggestMealForSoThinPeopleUI.showMealWithHighCalorie()
  verify { printer.printLine("-------------------------------------------------------------------------------------------------")
   printer.printLine("🔥 Feeling too thin? This meal above 700 calories!")
   printer.printLine("------------------------------------------------------------------------------------------------")
   printer.printLine("Name: ${meal.name}")
   printer.printLine("Description: ${meal.description}")
   printer.printLine("--------------------------------------------------------------------------------------------------")
   printer.printLine("Do you like it? (yes/no) 😊") }
   printer.printLine(INVALID_INPUT)
 }

 @Test
 fun `showMealWithHighCalorie() should accept uppercase YES as valid input`() {
  val meal = buildMeal(1, nutrition = buildNutrition(calories = 750f))
  every { getMealForSoThinPeople.suggestRandomMealForSoThinPeople() } returns Result.success(meal)
  every { reader.readStringOrNull() } returns "YES"

  suggestMealForSoThinPeopleUI.showMealWithHighCalorie()

  verify {
   printer.printLine("-------------------------------------------------------------------------------------------------")
   printer.printLine("🔥 Feeling too thin? This meal above 700 calories!")
   printer.printLine("------------------------------------------------------------------------------------------------")
   printer.printLine("Name: ${meal.name}")
   printer.printLine("Description: ${meal.description}")
   printer.printLine("--------------------------------------------------------------------------------------------------")
   printer.printLine("Do you like it? (yes/no) 😊")
  }
 }

 @Test
 fun `showMealWithHighCalorie() should show meals when user enter valid input but with leading and trailing space and ignoreCase`() {
  val meal = buildMeal(1, nutrition = buildNutrition(calories = 750f))
  every { getMealForSoThinPeople.suggestRandomMealForSoThinPeople() } returns Result.success(meal)
  every { reader.readStringOrNull() } returns " yEs  ".lowercase().trim()
  suggestMealForSoThinPeopleUI.showMealWithHighCalorie()

  verify {
   printer.printLine("-------------------------------------------------------------------------------------------------")
   printer.printLine("🔥 Feeling too thin? This meal above 700 calories!")
   printer.printLine("------------------------------------------------------------------------------------------------")
   printer.printLine("Name: ${meal.name}")
   printer.printLine("Description: ${meal.description}")
   printer.printLine("--------------------------------------------------------------------------------------------------")
   printer.printLine("Do you like it? (yes/no) 😊")
  }
 }

 @Test
 fun `showMealWithHighCalorie() should recall again when user enter no`() {
  val meal1 = buildMeal(1, nutrition = buildNutrition(calories = 750f))
  val meal2 = buildMeal(2, nutrition = buildNutrition(calories = 800f))

  every { getMealForSoThinPeople.suggestRandomMealForSoThinPeople() } returnsMany listOf(
   Result.success(meal1), Result.success(meal2)
  )
  every { reader.readStringOrNull() } returnsMany listOf(" NO", "yes")

  suggestMealForSoThinPeopleUI.showMealWithHighCalorie()

  verify(exactly = 2) { getMealForSoThinPeople.suggestRandomMealForSoThinPeople() }
 }

 @Test
 fun `showMealWithHighCalorie() should show no meal found with high calories if no meals are available`() {
  every {
   getMealForSoThinPeople.suggestRandomMealForSoThinPeople()
  } returns Result.failure(NoMealsForSoThinPeopleException(NO_MEAL_FOR_SO_THIN_PEOPLE))

  suggestMealForSoThinPeopleUI.showMealWithHighCalorie()

  verify { printer.printLine(NO_MEAL_FOR_SO_THIN_PEOPLE) }
 }

 @Test
 fun `showMealWithHighCalorie() should show invalid input message if input is null`() {
  val meal = buildMeal(1, nutrition = buildNutrition(calories = 750f))
  every { getMealForSoThinPeople.suggestRandomMealForSoThinPeople() } returns Result.success(meal)
  every { reader.readStringOrNull() } returns null

  suggestMealForSoThinPeopleUI.showMealWithHighCalorie()

  verify { printer.printLine(INVALID_INPUT) }
 }

}
