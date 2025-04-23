package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecases.utils.MealCreationHandler
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealsForGymHelperUseCase
import org.example.presentation.features.GymHelperUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GymHelperUITest {

    private lateinit var gymHelperUI: GymHelperUI
    private lateinit var getMealsForGymHelperUseCase: GetMealsForGymHelperUseCase
    private lateinit var mealCreationHandler: MealCreationHandler
    private lateinit var floatReader: InputReader<Float>
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        floatReader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getMealsForGymHelperUseCase = mockk(relaxed = true)
        gymHelperUI = GymHelperUI(getMealsForGymHelperUseCase, floatReader, printer)
        mealCreationHandler = MealCreationHandler()

    }

    @Test
    fun `useGymHelper() should print meals when call GetMealsForGymHelperUseCase with a valid proteins and calories with 20 percent difference`() {
        //Given
        val calories = 10F
        val proteins = 40F
        every { floatReader.read() } returns calories andThen proteins
        every {
            getMealsForGymHelperUseCase.getGymHelperMeals(
                calories, proteins, 20F
            )
        } returns Result.success(
            listOf(
                mealCreationHandler.getGymHelperMeal("meal1", 20F, 50F),
                mealCreationHandler.getGymHelperMeal("meal2", 9F, 45F),
                mealCreationHandler.getGymHelperMeal("meal3", 10F, 45F),
            )
        )
        //When
        gymHelperUI.useGymHelper()
        //Then

        verify { printer.printLine(mealCreationHandler.getGymHelperMeal("meal2", 9F, 45F).toString()) }
    }
}