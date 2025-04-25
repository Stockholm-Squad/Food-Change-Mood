package presentation.features
import org.example.logic.usecases.GetIraqiMealsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.Meal
import org.example.input_output.output.OutputPrinter
import org.example.model.FoodChangeMoodExceptions.LogicException.NoIraqiMeals
import org.example.presentation.features.GetIraqiMealsUI
import org.example.utils.Constants.NO_IRAQI_MEALS
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal

class GetIraqiMealsUITest {
    private lateinit var printer: OutputPrinter
    private lateinit var getIraqiMeals: GetIraqiMealsUseCase
    private lateinit var getIraqiMealsUI: GetIraqiMealsUI
    private lateinit var meal: Meal

    @BeforeEach
    fun setup() {
        printer = mockk(relaxed = true)
        getIraqiMeals = mockk(relaxed = true)
        meal = mockk(relaxed = true)
        getIraqiMealsUI = GetIraqiMealsUI(getIraqiMeals, printer)
    }

    @Test
    fun `showIraqiMeals() should print iraqi meals when have iraqi meals`() {
        // Arrange
        val meal = buildMeal(1, minutes = 30, name = "dolma", description = "iraqi")
        every { getIraqiMeals.getIraqiMeals() } returns Result.success(listOf(meal))
        // Act: Call the method to test
        getIraqiMealsUI.showIraqiMeals()
        verify {
            printer.printLine("🍽 Ready for some amazing Iraqi meals? Let's go!")
            printer.printLine("Name: ${meal.name}")
            printer.printLine("Time: ${meal.minutes}")
            printer.printLine("Description: ${meal.description ?: "No description available"}")
            printer.printLine("------------------------------------------------------------------------------")
        }
    }
    @Test
    fun `showIraqiMeals() should print iraqi meals when have iraqi meals with null description`() {
        // Arrange
        val meal = buildMeal(1, minutes = 30, name = "kabab", description = null, tags = listOf("iraqi"))
        every { getIraqiMeals.getIraqiMeals() } returns Result.success(listOf(meal))
        // Act: Call the method to test
        getIraqiMealsUI.showIraqiMeals()
        verify {
            printer.printLine("🍽 Ready for some amazing Iraqi meals? Let's go!")
            printer.printLine("Name: ${meal.name}")
            printer.printLine("Time: ${meal.minutes}")
            printer.printLine("Description: ${meal.description ?: "No description available"}")
            printer.printLine("------------------------------------------------------------------------------")
        }

    }
    @Test
    fun `showIraqiMeals() should print no found any iraqi meals when call and don't have iraqi meals`() {
        // Arrange
        every { getIraqiMeals.getIraqiMeals() } returns Result.failure(NoIraqiMeals(NO_IRAQI_MEALS))
        // Act: Call the method to test
        getIraqiMealsUI.showIraqiMeals()
        verify {   printer.printLine(NO_IRAQI_MEALS)
        }
    }
}
