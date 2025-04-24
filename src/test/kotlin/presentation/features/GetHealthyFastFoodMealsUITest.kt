package presentation.features


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetHealthyFastFoodUseCase
import org.example.presentation.features.GetHealthyFastFoodMealsUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import utils.buildNutrition
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
    fun `showHealthyFastFoodMeals () should print error message when use case fails`() {
        // Given
        every { getHealthyFastFoodUseCase.getHealthyFastFood() } returns Result.failure(Throwable())

        // When
        getHealthyFastFoodMealsUI.showHealthyFastFoodMeals()

        // Then
        verify { printer.printLine("${Constants.NO_MEALS_FOUND_MATCHING}") }
    }

    @Test
    fun `showHealthyFastFoodMeals () should print meal details when use case succeeds`() {
        // Given
        val meal = buildMeal(
            1,
            "souper  easy sweet   sour meatballs",
            100,
            nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
        )
        every { getHealthyFastFoodUseCase.getHealthyFastFood() } returns Result.success(listOf(meal))
        // When
        getHealthyFastFoodMealsUI.showHealthyFastFoodMeals()

        // Then
        verify { printer.printMeal(meal) }
    }
}