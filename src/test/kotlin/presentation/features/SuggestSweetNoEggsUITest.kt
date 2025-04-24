package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetDessertsWithNoEggsUseCase
import org.example.presentation.features.SuggestSweetNoEggsUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import kotlin.test.Test

class SuggestSweetNoEggsUITest {
    private lateinit var getDessertsWithNoEggsUseCase: GetDessertsWithNoEggsUseCase
    private lateinit var suggestSweetNoEggsUI: SuggestSweetNoEggsUI
    private lateinit var stringReader: InputReader
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        stringReader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getDessertsWithNoEggsUseCase = mockk(relaxed = true)
        suggestSweetNoEggsUI = SuggestSweetNoEggsUI(getDessertsWithNoEggsUseCase, stringReader, printer)

    }

    @Test
    fun `handleSweetsNoEggs() should show a dessert when desserts are available`() {
        //Given
        val meal = buildMeal(
            id = 1, name = "No-Egg Brownie", tags = listOf("desserts"), ingredients = listOf("flour", "suger")
        )
        every { getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs() } returns Result.success(listOf(meal))
        every { stringReader.readLineOrNull() } returns "y"
        //When
        suggestSweetNoEggsUI.handleSweetsNoEggs()
        //Then
        verify { printer.printLine(match { it.contains("Meal Name: No-Egg Brownie") }) }
    }

    @Test
    fun `handleSweetsNoEggs() should print fallback message when no desserts found`() {
        //Given
        every { getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs() } returns Result.failure(NoSuchElementException(""))
        //When
        suggestSweetNoEggsUI.handleSweetsNoEggs()
        //Then
        verify { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleSweetsNoEggs() should show a fallback message when user dislikes all desserts`() {
        // Given
        val meal1 = buildMeal(id = 1, name = "No-Egg Brownie", tags = listOf("desserts"))
        val meal2 = buildMeal(id = 2, name = "Pancake", tags = listOf("desserts"))

        every { getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs() } returns Result.success(listOf(meal1, meal2))
        every { stringReader.readLineOrNull() } returnsMany listOf("n", "n")

        // When
        suggestSweetNoEggsUI.handleSweetsNoEggs()

        // Then
        verify { printer.printLine(Constants.NO_MORE_DESSERTS_AVAILABLE) }
    }


}