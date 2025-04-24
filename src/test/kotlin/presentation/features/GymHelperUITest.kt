package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealsForGymHelperUseCase
import org.example.model.FoodChangeMoodExceptions
import org.example.presentation.features.GymHelperUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal
import utils.buildNutrition

class GymHelperUITest {

    private lateinit var gymHelperUI: GymHelperUI
    private lateinit var getMealsForGymHelperUseCase: GetMealsForGymHelperUseCase
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        reader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getMealsForGymHelperUseCase = mockk(relaxed = true)
        gymHelperUI = GymHelperUI(getMealsForGymHelperUseCase, reader, printer)

    }

    @Test
    fun `useGymHelper() should print meals when call GetMealsForGymHelperUseCase with a valid proteins and calories with 20 percent difference`() {
        //Given
        val calories = 10F
        val proteins = 40F
        every { reader.readFloatOrNull() } returns calories andThen proteins
        every {
            getMealsForGymHelperUseCase.getGymHelperMeals(
                calories, proteins, 20F
            )
        } returns Result.success(
            listOf(
                buildMeal(1, nutrition = buildNutrition(calories = 20F, protein = 50F)),
                buildMeal(2, nutrition = buildNutrition(calories = 9F, protein = 45F)),
                buildMeal(3, nutrition = buildNutrition(calories = 10F, protein = 45F)),
            )
        )

        //When
        gymHelperUI.useGymHelper()

        //Then
        verify { printer.printLine(buildMeal(2, nutrition = buildNutrition(calories = 9F, protein = 45F)).toString()) }
    }

    @Test
    fun `useGymHelper() should print no meals found when call GetMealsForGymHelperUseCase and No meals approximately matched the proteins and calories amounts`() {
        //Given
        val calories = 10F
        val proteins = 40F
        every { reader.readFloatOrNull() } returns calories andThen proteins
        every {
            getMealsForGymHelperUseCase.getGymHelperMeals(
                calories, proteins, 20F
            )
        } returns Result.failure(
            FoodChangeMoodExceptions.LogicException.NoMealsForGymHelperException(Constants.NO_MEALS_FOR_GYM_HELPER)
        )

        //When
        gymHelperUI.useGymHelper()

        //Then
        verify { printer.printLine(Constants.NO_MEALS_FOR_GYM_HELPER) }
    }

    @Test
    fun `useGymHelper() should print INVALID_INPUT when read invalid calories from user`() {
        //Given
        val calories = null
        val proteins = 40F
        every { reader.readFloatOrNull() } returns calories andThen proteins

        //When
        gymHelperUI.useGymHelper()

        //Then
        verify { printer.printLine(Constants.INVALID_INPUT) }
    }

    @Test
    fun `useGymHelper() should print INVALID_INPUT when read invalid proteins from user`() {
        //Given
        val calories = 10F
        val proteins = null
        every { reader.readFloatOrNull() } returns calories andThen proteins

        //When
        gymHelperUI.useGymHelper()

        //Then
        verify { printer.printLine(Constants.INVALID_INPUT) }
    }

}