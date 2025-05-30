package presentation.features


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.createListOfMeals
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetHealthyFastFoodUseCase
import org.example.model.FoodChangeMoodExceptions
import org.example.presentation.features.GetHealthyFastFoodMealsUI
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetHealthyFastFoodMealsUITest {
    private lateinit var getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase
    private lateinit var getHealthyFastFoodMealsUI: GetHealthyFastFoodMealsUI
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        getHealthyFastFoodUseCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getHealthyFastFoodMealsUI = GetHealthyFastFoodMealsUI(getHealthyFastFoodUseCase, printer)
    }

    @Test
    fun `showHealthyFastFoodMeals() should print error message when use case fails`() {
        // Given
        every { getHealthyFastFoodUseCase.getHealthyFastFood() } returns Result.failure(FoodChangeMoodExceptions.LogicException.NoMealsFound())

        // When
        getHealthyFastFoodMealsUI.showHealthyFastFoodMeals()

        // Then
        verify { printer.printLine("${FoodChangeMoodExceptions.LogicException.NoMealsFound().message}") }
    }

    @Test
    fun `showHealthyFastFoodMeals() should print meal details when use case succeeds`() {
        // Given
        val meals = createListOfMeals()
        every { getHealthyFastFoodUseCase.getHealthyFastFood() } returns Result.success(meals)
        // When
        getHealthyFastFoodMealsUI.showHealthyFastFoodMeals()

        // Then
        verify {printer.printMeals(meals) }
    }
}