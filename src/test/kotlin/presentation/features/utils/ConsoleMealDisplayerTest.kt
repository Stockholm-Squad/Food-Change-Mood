package presentation.features.utils

import org.junit.jupiter.api.Assertions.*

import io.mockk.*
import org.example.input_output.output.OutputPrinter
import org.example.presentation.features.utils.ConsoleMealDisplayer
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal

class ConsoleMealDisplayerTest {

    private lateinit var printer: OutputPrinter
    private lateinit var consoleMealDisplayer: ConsoleMealDisplayer

    @BeforeEach
    fun setUp() {
        printer = mockk(relaxed = true)
        consoleMealDisplayer = ConsoleMealDisplayer(printer)
    }

    @Test
    fun `display prints meal header and meal details`() {
        // Given
        val meal = buildMeal(1, "Cheese Pizza")

        // When
        consoleMealDisplayer.display(meal)

        // Then
        verify {
            printer.printLine(Constants.MEAL_DETAILS_HEADER.format("Cheese Pizza"))
            printer.printMeal(meal)
        }
    }
}