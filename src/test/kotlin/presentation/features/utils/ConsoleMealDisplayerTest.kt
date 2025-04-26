package presentation.features.utils

import io.mockk.*
import model.Meal
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
    fun `display() should print meal header and meal details when meal is not null`() {
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

    @Test
    fun `display() should print no meal message when meal is null`() {
        // Given
        val meal: Meal? = null

        // When
        consoleMealDisplayer.display(meal)

        // Then
        verify {
            printer.printLine("No meal to display.")
        }
    }

    @Test
    fun `display() should skip header when meal name is null`() {
        // Given
        val meal = buildMeal(id = 1, name = null)

        // When
        consoleMealDisplayer.display(meal)

        // Then
        verify(exactly = 0) { printer.printLine(any()) }
        verify { printer.printMeal(meal) }
    }
}
