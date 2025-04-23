package presentation.features

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.usecases.utils.buildMeal
import model.Meal
import org.example.logic.usecases.GetMealsByDateUseCase
import org.example.presentation.features.SearchByAddDateUI
import org.example.utils.DateValidator
import org.example.utils.getDateFromString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class SearchByAddDateUITest {

    private lateinit var getMealsByDateUseCase: GetMealsByDateUseCase
    private lateinit var dateValidator: DateValidator
    private lateinit var searchByAddDateUI: SearchByAddDateUI

    // Redirect System.out to capture console output
    private val outContent = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        getMealsByDateUseCase = mockk(relaxed = true)
        dateValidator = mockk(relaxed = true)
        searchByAddDateUI = SearchByAddDateUI(getMealsByDateUseCase, dateValidator)

        // Redirect System.out to capture output
        System.setOut(PrintStream(outContent))
    }

    @AfterEach
    fun tearDown() {
        // Restore original System.out
        System.setOut(originalOut)
    }

    @Test
    fun `should handle valid date input and display meals`() {
        // Given
        val date = "2023-10-01"
        val meal1 = buildMeal(id = 1, name = "Spaghetti Carbonara")
        every { dateValidator.isValidDate(date) } returns true
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal1))

        // Simulate user input (valid date followed by -1 to exit)
        val userInput = "$date\n-1\n0\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02")
        assertThat(output).contains("Loading...")
        assertThat(output).contains("1 -> Spaghetti Carbonara")
        assertThat(output).contains("-1 -> search again or back")
    }

    @Test
    fun `should handle invalid date input gracefully`() {
        // Given
        val invalidDate = "invalid-date"
        every { dateValidator.isValidDate(invalidDate) } returns false

        // Simulate user input (invalid date followed by 0 to exit)
        val userInput = "$invalidDate\n0\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02")
        assertThat(output).contains("Enter a valid Date or zero => 0")
    }

    @Test
    fun `should exit gracefully when user enters 0`() {
        // Given
        val userInput = "0\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02")
        assertThat(output).doesNotContain("Loading...")
    }

    @Test
    fun `should handle empty repository response gracefully`() {
        // Given
        val date = "2023-10-01"
        every { dateValidator.isValidDate(date) } returns true
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(emptyList<Meal>())

        // Simulate user input (valid date followed by 0 to exit)
        val userInput = "$date\n-1\n0\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02")
        assertThat(output).contains("Loading...")
        assertThat(output.split("->").size - 1).isEqualTo(2)
    }

    @Test
    fun `should handle repository failure gracefully`() {
        // Given
        val date = "2023-10-01"
        val exception = Exception("Database error")
        every { dateValidator.isValidDate(date) } returns true
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.failure(exception)

        // Simulate user input (valid date followed by 0 to exit)
        val userInput = "$date\n-1\n0\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02")
        assertThat(output).contains("Loading...")
        assertThat(output).contains("Database error") // Error message should be displayed
    }

    @Test
    fun `should handle user interaction in handleUserInteraction`() {
        // Given
        val date = "2023-10-01"
        val meal1 = buildMeal(id = 1, name = "Spaghetti Carbonara")
        every { dateValidator.isValidDate(date) } returns true
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal1))

        // Simulate user input (valid date, valid meal ID, then -1 to exit)
        val userInput = "$date\n1\n-1\n0\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02")
        assertThat(output).contains("Loading...")
        assertThat(output).contains("1 -> Spaghetti Carbonara")
        assertThat(output).contains("id= 1,")
        assertThat(output).contains("-1 -> search again or back")
    }

    @Test
    fun `should handle invalid meal ID input gracefully`() {
        // Given
        val date = "2023-10-01"
        val meal1 = buildMeal(id = 1, name = "Spaghetti Carbonara")
        every { dateValidator.isValidDate(date) } returns true
        every { getMealsByDateUseCase.getMealsByDate(date) } returns Result.success(listOf(meal1))

        // Simulate user input (valid date, invalid meal ID, then -1 to exit)
        val userInput = "$date\n99\n-1\n0\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        searchByAddDateUI.searchMealsByDate()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("📅 Enter date (YYYY-MM-DD): ex: 2002-02-02")
        assertThat(output).contains("Loading...")
        assertThat(output).contains("1 -> Spaghetti Carbonara")
        assertThat(output).contains("The meal with ID 99 does not exist.")
        assertThat(output).contains("-1 -> search again or back")
    }
}