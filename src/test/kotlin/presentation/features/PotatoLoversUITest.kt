package presentation.features

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import logic.usecases.buildMeal
import model.Nutrition
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.presentation.features.PotatoLoversUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PotatoLoversUITest {

    private lateinit var potatoMeals: GetPotatoMealsUseCase
    private lateinit var potatoMealUi: PotatoLoversUI
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var inputReader: InputReader

    @BeforeEach
    fun setUp() {
        potatoMeals = mockk(relaxed = true)
        outputPrinter = mockk(relaxed = true)
        inputReader = mockk()
        potatoMealUi = PotatoLoversUI(
            potatoMeals,
            outputPrinter,
            inputReader
        )
    }

    @Test
    fun `should print intro and call useCase when showPotatoLoversUI is called`() {
        // Given
        every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(emptyList())
        every { inputReader.readLineOrNull() } returns "n"

        // When
        potatoMealUi.showPotatoLoversUI(5)

        // Then
        verify { outputPrinter.printLine(Constants.I_LOVE_POTATO_HERE + "5 " + Constants.MEAL_INCLUDE_POTATO + "\n") }
        verify { potatoMeals.getRandomPotatoMeals(5) }
        verify { outputPrinter.printLine(Constants.SEE_MORE_MEALS) }
        verify { outputPrinter.printLine(Constants.ENJOY_YOUR_MEAL) }
    }

    @Test
    fun `should repeat UI if user enters yes`() {
        // Given
        every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(emptyList())
        every { inputReader.readLineOrNull() } returnsMany listOf("yes", "n")

        // When
        potatoMealUi.showPotatoLoversUI(3)

        // Then
        verify(exactly = 2) { potatoMeals.getRandomPotatoMeals(3) }
        verify(exactly = 2) { outputPrinter.printLine(Constants.SEE_MORE_MEALS) }
        verify { outputPrinter.printLine(Constants.ENJOY_YOUR_MEAL) }
    }

    @Test
    fun `should not repeat UI if user enters anything other than yes`() {
        // Given
        every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(emptyList())
        every { inputReader.readLineOrNull() } returns "no"

        // When
        potatoMealUi.showPotatoLoversUI(2)

        // Then
        verify(exactly = 1) { potatoMeals.getRandomPotatoMeals(2) }
        verify { outputPrinter.printLine(Constants.ENJOY_YOUR_MEAL) }
    }

    @Test
    fun `should handle null input without crashing`() {
        // Given
        every { potatoMeals.getRandomPotatoMeals(any()) } returns Result.success(emptyList())
        every { inputReader.readLineOrNull() } returns null

        // When
        potatoMealUi.showPotatoLoversUI()

        // Then
        verify { potatoMeals.getRandomPotatoMeals(10) }
        verify { outputPrinter.printLine(Constants.ENJOY_YOUR_MEAL) }
    }


}