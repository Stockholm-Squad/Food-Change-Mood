package presentation.features

import io.mockk.*
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealForKetoDietUseCase
import org.example.presentation.features.KetoDietMealUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal
import utils.buildNutrition

class KetoDietMealUITest {

    private lateinit var useCase: GetMealForKetoDietUseCase
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter
    private lateinit var ui: KetoDietMealUI

    private val ketoMeal = buildMeal(
        id = 1,
        name = "Keto Chicken",
        description = "High-fat, low-carb chicken meal.",
        nutrition = buildNutrition(
            carbohydrates = 5f,
            totalFat = 20f,
            protein = 25f,
        ),
        ingredients = listOf("Chicken", "Avocado Oil"),
        contributorId = 1,
    )

    @BeforeEach
    fun setup() {
        useCase = mockk()
        reader = mockk()
        printer = mockk(relaxed = true)
        ui = KetoDietMealUI(useCase, reader, printer)
    }

    @Test
    fun `showKetoMeal() should show message when no more keto meals are available`() {
        //given
        every { useCase.getKetoMeal() } returns Result.success(null)
        //when
        ui.showKetoMeal()
        //then
        verify { printer.printLine(Constants.FINISH_MEALS) }
    }

    @Test
    fun `showKetoMeal() should show meal and handle positive feedback`() {
        //given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returns "yes"
        //when
        ui.showKetoMeal()
        //given
        verify {
            printer.printLine(match { it.contains(ketoMeal.name.toString())})
            printer.printLine(match { it.contains(ketoMeal.description.toString())})
            printer.printLine(match { it.contains(Constants.YES_ANSWER)})
        }
    }

    @Test
    fun `showKetoMeal() should recursively call showKetoMeal on negative feedback`() {
        //given
        every { useCase.getKetoMeal() } returnsMany listOf(Result.success(ketoMeal), Result.success(null))
        every { reader.readLineOrNull() } returnsMany listOf("no")
        //when
        ui.showKetoMeal()
        //then
        verify(atLeast = 2) { useCase.getKetoMeal() }
        verify { printer.printLine(Constants.FINISH_MEALS) }
    }

    @Test
    fun `showKetoMeal() should prompt again on invalid input`() {
        //given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returnsMany listOf("maybe", "yes")
        //when
        ui.showKetoMeal()
        //then
        verify {
            printer.printLine(Constants.INVALID_INPUT)
            printer.printLine(match { it.contains(Constants.YES_ANSWER)})
        }
    }

    @Test
    fun `showKetoMeal should show finish message when no more meals are available`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.success(null)

        // When
        ui.showKetoMeal()

        // Then
        verify(exactly = 1) { printer.printLine(Constants.START_MESSAGE) }
        verify(exactly = 1) { printer.printLine(Constants.FINISH_MEALS) }
    }

    @Test
    fun `showKetoMeal should display meal details and handle yes response`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returns "yes"

        // When
        ui.showKetoMeal()

        // Then
        verifyOrder {
            printer.printLine(Constants.START_MESSAGE)
            printer.printLine(match { it.contains(ketoMeal.name.toString()) })
            printer.printLine(match { it.contains(ketoMeal.description.toString()) })
            printer.printLine(match { it.contains("${ketoMeal.nutrition?.carbohydrates}g") })
            printer.printLine(Constants.ASK_YES_NO)
            printer.printLine(match { it.contains(Constants.YES_ANSWER) })
        }
        verify(exactly = 0) { printer.printLine(Constants.FINISH_MEALS) }
    }

    @Test
    fun `showKetoMeal should recursively call itself on no response`() {
        // Given
        every { useCase.getKetoMeal() } returnsMany listOf(
            Result.success(ketoMeal),
            Result.success(null)
        )
        every { reader.readLineOrNull() } returns "no"

        // When
        ui.showKetoMeal()

        // Then
        verify(exactly = 2) { useCase.getKetoMeal() }
        verifyOrder {
            printer.printLine(Constants.ASK_YES_NO)
            printer.printLine(Constants.FINISH_MEALS)
        }
    }
    @Test
    fun `showKetoMeal should reprompt on invalid input`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returnsMany listOf("maybe", "yes")

        // When
        ui.showKetoMeal()

        // Then
        verifyOrder {
            printer.printLine(Constants.ASK_YES_NO)
            printer.printLine(Constants.INVALID_INPUT)
            printer.printLine(Constants.ASK_YES_NO)
            printer.printLine(match { it.contains(Constants.YES_ANSWER) })
        }
        verify(exactly = 1) { useCase.getKetoMeal() }
    }
    @Test
    fun `showKetoMeal should show error message when use case fails`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.failure(RuntimeException("Network error"))

        // When
        ui.showKetoMeal()

        // Then
        verify(exactly = 1) { printer.printLine(Constants.START_MESSAGE) }
        verify(exactly = 1) { printer.printLine(Constants.ERROR_FETCHING_MEALS) }
        verify(exactly = 0) { printer.printLine(Constants.ASK_YES_NO) }
        verify(exactly = 0) { printer.printLine(Constants.FINISH_MEALS) }
    }
    @Test
    fun `showKetoMeal should handle case-insensitive input`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returns "YES" // Uppercase

        // When
        ui.showKetoMeal()

        // Then
        verify {
            printer.printLine(match { it.contains(Constants.YES_ANSWER) })
        }
        verify(exactly = 0) { printer.printLine(Constants.INVALID_INPUT) }
    }
    @Test
    fun `showKetoMeal should handle empty input`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returnsMany listOf("", "yes")

        // When
        ui.showKetoMeal()

        // Then
        verify(exactly = 1) { printer.printLine(Constants.INVALID_INPUT) }
        verify(exactly = 2) { printer.printLine(Constants.ASK_YES_NO) }
    }

    @Test
    fun `showKetoMeal should handle null nutrition values`() {
        // Given
        val mealWithNullNutrition = ketoMeal.copy(nutrition = null)
        every { useCase.getKetoMeal() } returns Result.success(mealWithNullNutrition)
        every { reader.readLineOrNull() } returns "yes"

        // When
        ui.showKetoMeal()

        // Then
        verify {
            printer.printLine(match { it.contains("Suggested Keto Meal") })
            printer.printLine(match { !it.contains("Nutrition") }) // No nutrition line
        }
    }



    @Test
    fun `showKetoMeal should skip feedback when meal name is null`() {
        // Given
        val mealWithNullName = ketoMeal.copy(name = null)
        every { useCase.getKetoMeal() } returns Result.success(mealWithNullName)

        // When
        ui.showKetoMeal()

        // Then
        verify(exactly = 0) { printer.printLine(Constants.ASK_YES_NO) }
        verify(exactly = 0) { reader.readLineOrNull() }
    }

    @Test
    fun `showKetoMeal should trim input with whitespace`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returns "  yes  "

        // When
        ui.showKetoMeal()

        // Then
        verify {
            printer.printLine(match { it.contains(Constants.YES_ANSWER) })
        }
        verify(exactly = 0) { printer.printLine(Constants.INVALID_INPUT) }
    }
    @Test
    fun `showKetoMeal should handle mixed case input`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returns "YeS"

        // When
        ui.showKetoMeal()

        // Then
        verify {
            printer.printLine(match { it.contains(Constants.YES_ANSWER) })
        }
        verify(exactly = 0) { printer.printLine(Constants.INVALID_INPUT) }
    }

    @Test
    fun `showKetoMeal should handle null input as invalid`() {
        // Given
        every { useCase.getKetoMeal() } returns Result.success(ketoMeal)
        every { reader.readLineOrNull() } returnsMany listOf(null, "yes")

        // When
        ui.showKetoMeal()

        // Then
        verifyOrder {
            printer.printLine(Constants.ASK_YES_NO)
            printer.printLine(Constants.INVALID_INPUT)
            printer.printLine(Constants.ASK_YES_NO)
            printer.printLine(match { it.contains(Constants.YES_ANSWER) })
        }
    }

}
