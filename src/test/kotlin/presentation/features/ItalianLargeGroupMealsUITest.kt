package presentation.features

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.usecases.utils.buildMeal
import org.example.logic.usecases.GetItalianMealsForLargeGroupUseCase
import org.example.presentation.features.ItalianLargeGroupMealsUI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ItalianLargeGroupMealsUITest {

    private lateinit var getItalianMealsForLargeGroupUseCase: GetItalianMealsForLargeGroupUseCase
    private lateinit var italianLargeGroupMealsUI: ItalianLargeGroupMealsUI

    // Redirect System.out to capture console output
    private val outContent = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        // Mock the use case
        getItalianMealsForLargeGroupUseCase = mockk(relaxed = true)
        italianLargeGroupMealsUI = ItalianLargeGroupMealsUI(getItalianMealsForLargeGroupUseCase)

        // Redirect System.out to capture output
        System.setOut(PrintStream(outContent))
    }

    @AfterEach
    fun tearDown() {
        // Restore original System.out
        System.setOut(originalOut)
    }

    @Test
    fun `italianLargeGroupMealsUI() should display meals when use case returns success`() {
        // Given
        val meal1 = buildMeal(id = 1, name = "Spaghetti Carbonara", tags = listOf("italian", "for-large-groups"))
        val meal2 = buildMeal(id = 2, name = "Pizza Margherita", tags = listOf("italian", "for-large-groups"))
        every { getItalianMealsForLargeGroupUseCase.getMeals() } returns Result.success(listOf(meal1, meal2))

        // Simulate user input (-1 to exit)
        val userInput = "-1\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("🍝 Planning a big Italian feast?")
        assertThat(output).contains("1 -> Spaghetti Carbonara")
        assertThat(output).contains("2 -> Pizza Margherita")
        assertThat(output).contains("-1 -> back")
    }

    @Test
    fun `italianLargeGroupMealsUI() should handle failure when use case fails`() {
        // Given
        val exception = Exception("Database error")
        every { getItalianMealsForLargeGroupUseCase.getMeals() } returns Result.failure(exception)

        // Simulate user input (-1 to exit)
        val userInput = "-1\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("error: java.lang.Exception: Database error")
        assertThat(output).contains("-1 -> back")
    }

    @Test
    fun `italianLargeGroupMealsUI() should handle valid meal id input and display details`() {
        // Given
        val meal1 = buildMeal(id = 1, name = "Spaghetti Carbonara", tags = listOf("italian", "for-large-groups"))
        every { getItalianMealsForLargeGroupUseCase.getMeals() } returns Result.success(listOf(meal1))

        // Simulate user input (valid meal ID followed by -1 to exit)
        val userInput = "1\n-1\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("id= 1")
        assertThat(output).contains("name= Spaghetti Carbonara")
        assertThat(output).contains("-1 -> back")
    }

    @Test
    fun `italianLargeGroupMealsUI() should handle invalid meal id input gracefully`() {
        // Given
        val meal1 = buildMeal(id = 1, name = "Spaghetti Carbonara", tags = listOf("italian", "for-large-groups"))
        every { getItalianMealsForLargeGroupUseCase.getMeals() } returns Result.success(listOf(meal1))

        // Simulate user input (invalid meal ID followed by -1 to exit)
        val userInput = "99\n-1\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("The meal with ID 99 does not exist.")
        assertThat(output).contains("-1 -> back")
    }

    @Test
    fun `italianLargeGroupMealsUI() should handle empty user input gracefully`() {
        // Given
        val meal1 = buildMeal(
            id = 1,
            name = "Spaghetti Carbonara",
            tags = listOf("italian", "for-large-groups")
        )
        every { getItalianMealsForLargeGroupUseCase.getMeals() } returns Result.success(listOf(meal1))

        // Simulate user input (empty input followed by -1 to exit)
        val userInput = "\n-1\n"
        System.setIn(ByteArrayInputStream(userInput.toByteArray()))

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        val output = outContent.toString()
        assertThat(output).contains("Enter a valid ID or -1")
        assertThat(output).contains("-1 -> back")
    }

}