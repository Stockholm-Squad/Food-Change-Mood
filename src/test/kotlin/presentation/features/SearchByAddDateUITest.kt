package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealsByDateUseCase
import org.example.presentation.features.SearchByAddDateUI
import org.example.utils.Constants.ENTER_VALID_DATE
import org.example.utils.Constants.NO_MEALS_FOUND_WITH_THIS_DATE
import org.example.utils.DateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal

class SearchByAddDateUITest {
    private lateinit var getMealsByDateUseCase: GetMealsByDateUseCase
    private lateinit var dateValidator: DateValidator
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter
    private lateinit var searchByAddDateUI: SearchByAddDateUI

    @BeforeEach
    fun setUp() {
        getMealsByDateUseCase = mockk(relaxed = true)
        dateValidator = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        searchByAddDateUI = SearchByAddDateUI(getMealsByDateUseCase, dateValidator, reader, printer)

    }

    @Test
    fun `searchMealsByDate should display meals when given valid date input`() {
        // Given
        val date = "2023-10-01"
        val meal = buildMeal(id = 1, name = "Pasta")
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readStringOrNull() } returnsMany listOf(date, "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 2) { printer.printLine("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02\n or 0 to exit") }
        verify(exactly = 1) { printer.printLine("Loading...") }
        verify(exactly = 1) { printer.printLine("1 -> Pasta") }
        verify(exactly = 1) { getMealsByDateUseCase.getMealsByDate(date) }
    }

    @Test
    fun `searchMealsByDate should show error message when given invalid date format`() {
        // Given
        val invalidDate = "invalid-date"
        every { dateValidator.isValidDate(invalidDate) } returns false
        every { reader.readStringOrNull() } returnsMany listOf(invalidDate, "0")

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine(ENTER_VALID_DATE) }
        verify(exactly = 0) { getMealsByDateUseCase.getMealsByDate(any()) }
    }

    @Test
    fun `searchMealsByDate should exit when user enters 0`() {
        // Given
        every { reader.readStringOrNull() } returns "0"

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02\n or 0 to exit") }
        verify(exactly = 0) { getMealsByDateUseCase.getMealsByDate(any()) }
    }

    @Test
    fun `searchMealsByDate should show empty message when no meals found`() {
        // Given
        val date = "2023-10-01"
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readStringOrNull() } returnsMany listOf(date, "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(emptyList())

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("Loading...") }
        verify(exactly = 1) { printer.printLine(NO_MEALS_FOUND_WITH_THIS_DATE) }
    }

    @Test
    fun `searchMealsByDate should show error message when repository fails`() {
        // Given
        val date = "2023-10-01"
        val exception = Exception("Database error")
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readStringOrNull() } returnsMany listOf(date, "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.failure(exception)

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("error: Database error") }
    }

    @Test
    fun `searchMealsByDate should show error message when repository fails with no message`() {
        // Given
        val date = "2023-10-01"
        val exception = Exception()
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readStringOrNull() } returnsMany listOf(date, "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.failure(exception)

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("error: Unknown Error") }
    }

    @Test
    fun `searchMealsByDate should show meal details when valid meal ID is entered`() {
        // Given
        val date = "2023-10-01"
        val meal = buildMeal(id = 1, name = "Pizza")
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readStringOrNull() } returnsMany listOf(date, "1", "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printMeal(meal) }
    }

    @Test
    fun `searchMealsByDate should show error when invalid meal ID is entered`() {
        // Given
        val date = "2023-10-01"
        val meal = buildMeal(id = 1, name = "Pizza")
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readStringOrNull() } returnsMany listOf(date, "99", "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("The meal with ID 99 does not exist.") }
    }


    @Test
    fun `searchMealsByDate should handle null date input gracefully`() {
        // Given
        every { reader.readStringOrNull() } returnsMany listOf(null, "0") // First input is null

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine(ENTER_VALID_DATE) }
        verify(exactly = 0) { getMealsByDateUseCase.getMealsByDate(any()) }
    }


    @Test
    fun `searchMealsByDate should continue when meal ID input is null`() {
        // Given
        val date = "2023-10-01"
        val meal = buildMeal(id = 1, name = "Lasagna")
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readStringOrNull() } returnsMany listOf(date, null, "-1", "0")

        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("Enter a valid ID or -1") }
        verify(exactly = 1) { printer.printLine("1 -> Lasagna") }
    }

    @Test
    fun `viewMealInListDetails should print meal details when meal exists`() {
        // Given
        val mealId = 1
        val meal = buildMeal(id = mealId, name = "Pasta")
        val meals = listOf(meal)
        val printer = mockk<OutputPrinter>(relaxed = true)

        // When
        searchByAddDateUI.viewMealInListDetails(mealId, meals, printer)

        // Then
        verify(exactly = 1) { printer.printMeal(meal) }
    }

    @Test
    fun `viewMealInListDetails should print not found message when meal does not exist`() {
        // Given
        val mealId = 99
        val meal = buildMeal(id = 1, name = "Pasta")
        val meals = listOf(meal)
        val printer = mockk<OutputPrinter>(relaxed = true)

        // When
        searchByAddDateUI.viewMealInListDetails(mealId, meals, printer)

        // Then
        verify(exactly = 1) {
            printer.printLine("The meal with ID $mealId does not exist.")
        }
        verify(exactly = 0) { printer.printMeal(any()) }
    }

    @Test
    fun `viewMealInListDetails should handle empty list gracefully`() {
        // Given
        val mealId = 1
        val meals = emptyList<Meal>()
        val printer = mockk<OutputPrinter>(relaxed = true)

        // When
        searchByAddDateUI.viewMealInListDetails(mealId, meals, printer)

        // Then
        verify(exactly = 1) {
            printer.printLine("The meal with ID $mealId does not exist.")
        }
        verify(exactly = 0) { printer.printMeal(any()) }
    }
}