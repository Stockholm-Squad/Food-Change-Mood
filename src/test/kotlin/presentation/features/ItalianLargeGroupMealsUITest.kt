package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetItalianMealsForLargeGroupUseCase
import org.example.presentation.features.ItalianLargeGroupMealsUI
import org.example.utils.Constants.NO_ITALIAN_MEALS_FOR_LARGE_GROUP_FOUND
import org.example.utils.viewMealInListDetails
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal

class ItalianLargeGroupMealsUITest {
    private lateinit var getItalianMealsForLargeGroupUseCase: GetItalianMealsForLargeGroupUseCase
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter
    private lateinit var italianLargeGroupMealsUI: ItalianLargeGroupMealsUI

    @BeforeEach
    fun setUp() {
        // Mock the use case
        getItalianMealsForLargeGroupUseCase = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        italianLargeGroupMealsUI = ItalianLargeGroupMealsUI(getItalianMealsForLargeGroupUseCase, reader, printer)
    }


    @Test
    fun `italianLargeGroupMealsUI should display meals when use case returns success`() {
        // Given
        val meal1 = buildMeal(id = 1, name = "Pasta", tags = listOf("italian", "for-large-groups"))
        val meal2 = buildMeal(id = 2, name = "Pizza", tags = listOf("italian", "for-large-groups"))
        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.success(
            listOf(
                meal1,
                meal2
            )
        )
        every { reader.readStringOrNull() } returns "-1"

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) { printer.printLine("1 -> Pasta") }
        verify(exactly = 1) { printer.printLine("2 -> Pizza") }
    }

    @Test
    fun `italianLargeGroupMealsUI should display error message when use case fails`() {
        // Given
        val exception = Exception("Database error")
        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.failure(exception)
        every { reader.readStringOrNull() } returns "-1"

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) { printer.printLine("Error: Database error") }
    }

    @Test
    fun `italianLargeGroupMealsUI should display error message when use case fails with no message`() {
        // Given
        val exception = Exception()
        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.failure(exception)
        every { reader.readStringOrNull() } returns "-1"

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) { printer.printLine("Error: Unknown Error") }
    }

    @Test
    fun `italianLargeGroupMealsUI should show meal details when valid meal ID is entered`() {
        // Given
        val meal = buildMeal(id = 1, name = "Lasagna", tags = listOf("italian", "for-large-groups"))

        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.success(listOf(meal))
        every { reader.readStringOrNull() } returnsMany listOf("1", "-1")


        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) { listOf(meal).viewMealInListDetails(1, printer) }
    }

    @Test
    fun `italianLargeGroupMealsUI should show error when invalid meal ID is entered`() {
        // Given
        val meal = buildMeal(id = 1, name = "Risotto", tags = listOf("italian", "for-large-groups"))
        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.success(listOf(meal))
        every { reader.readStringOrNull() } returnsMany listOf("99", "-1")

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) { printer.printLine("The meal with ID 99 does not exist.") }
    }


    @Test
    fun `italianLargeGroupMealsUI should display empty message when no meals found`() {
        // Given
        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.success(emptyList())
        every { reader.readStringOrNull() } returns "-1"

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) { printer.printLine(NO_ITALIAN_MEALS_FOR_LARGE_GROUP_FOUND) }
    }


    @Test
    fun `italianLargeGroupMealsUI() should handle empty user input gracefully`() {
        // Given
        val meal1 = buildMeal(id = 1, name = "Spaghetti Carbonara", tags = listOf("italian", "for-large-groups"))
        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.success(listOf(meal1))
        every { reader.readStringOrNull() } returnsMany listOf("", "-1")

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) { printer.printLine("Enter a valid ID or -1") }
    }

    @Test
    fun `italianLargeGroupMealsUI should display header and loading message when started`() {
        // Given
        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.success(emptyList())
        every { reader.readStringOrNull() } returns "-1"

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) {
            printer.printLine("🍝 Planning a big Italian feast? Here's a list of meals perfect for large groups:")
        }
        verify(exactly = 1) { printer.printLine("Loading...") }
    }

    @Test
    fun `italianLargeGroupMealsUI should continue when meal ID input is null`() {
        // Given
        val meal = buildMeal(id = 1, name = "Fettuccine", tags = listOf("italian", "for-large-groups"))
        every { getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup() } returns Result.success(listOf(meal))
        every { reader.readStringOrNull() } returnsMany listOf(null, "-1")

        // When
        italianLargeGroupMealsUI.italianLargeGroupMealsUI()

        // Then
        verify(exactly = 1) { printer.printLine("1 -> Fettuccine") }
        verify(exactly = 1) { printer.printLine("Enter a valid ID or -1") }
    }
}