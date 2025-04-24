package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealsByDateUseCase
import org.example.presentation.features.SearchByAddDateUI
import org.example.utils.Constants.NO_MEALS_FOUND_WITH_THIS_DATE
import org.example.utils.DateValidator
import org.example.utils.viewMealInListDetails
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
        every { reader.readLine() } returnsMany listOf(date, "-1", "0")
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
        every { reader.readLine() } returnsMany listOf(invalidDate, "0")

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("Enter a valid Date or zero => 0") }
        verify(exactly = 0) { getMealsByDateUseCase.getMealsByDate(any()) }
    }

    @Test
    fun `searchMealsByDate should exit when user enters 0`() {
        // Given
        every { reader.readLine() } returns "0"

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
        every { reader.readLine() } returnsMany listOf(date, "-1", "0")
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
        every { reader.readLine() } returnsMany listOf(date, "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.failure(exception)

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("error: Database error") }
    }

    @Test
    fun `searchMealsByDate should show meal details when valid meal ID is entered`() {
        // Given
        val date = "2023-10-01"
        val meal = buildMeal(id = 1, name = "Pizza")
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readLine() } returnsMany listOf(date, "1", "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("1 -> Pizza") }
        verify(exactly = 1) { listOf(meal).viewMealInListDetails(1, printer) }
    }

    @Test
    fun `searchMealsByDate should show error when invalid meal ID is entered`() {
        // Given
        val date = "2023-10-01"
        val meal = buildMeal(id = 1, name = "Pizza")
        every { dateValidator.isValidDate(date) } returns true
        every { reader.readLine() } returnsMany listOf(date, "99", "-1", "0")
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        verify(exactly = 1) { printer.printLine("The meal with ID 99 does not exist.") }
    }
}