package presentation.features

import io.mockk.*
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealForKetoDietUseCase
import org.example.presentation.features.KetoDietMealUI
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
        verify { printer.printLine("✔️ You've seen all available keto meals!") }
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
            printer.printLine("🍽 Suggested Keto Meal: ${ketoMeal.name}")
            printer.printLine("📋 Description: ${ketoMeal.description}")
            printer.printLine("👍 Great, enjoy your meal: ${ketoMeal.name}")
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
        verify { printer.printLine("✔️ You've seen all available keto meals!") }
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
            printer.printLine("⚠️ Invalid input. Please answer with 'yes' or 'no'.")
            printer.printLine("👍 Great, enjoy your meal: ${ketoMeal.name}")
        }
    }
}
