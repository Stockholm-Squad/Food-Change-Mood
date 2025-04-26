package org.example.presentation.features

import io.mockk.*
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.presentation.features.utils.ConsoleMealDisplayer
import org.example.presentation.features.utils.SearchUtils
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal


class PotatoLoversUITest {

    private lateinit var getPotatoMealsUseCase: GetPotatoMealsUseCase
    private lateinit var printer: OutputPrinter
    private lateinit var reader: InputReader
    private lateinit var searchUtils: SearchUtils
    private lateinit var mealDisplayer: ConsoleMealDisplayer

    private lateinit var potatoLoversUI: PotatoLoversUI

    @BeforeEach
    fun setUp() {
        getPotatoMealsUseCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        searchUtils = mockk(relaxed = true)
        mealDisplayer = mockk(relaxed = true)

        potatoLoversUI = PotatoLoversUI(
            getPotatoMealsUseCase = getPotatoMealsUseCase,
            printer = printer,
            reader = reader,
            searchUtils = searchUtils,
            mealDisplayer = mealDisplayer
        )
    }


//    @Test
//    fun `should display no meals found message when no meals are returned`() {
//        every { getPotatoMealsUseCase.getRandomPotatoMeals(10) } returns Result.success(emptyList())
//
//        potatoLoversUI.showPotatoLoversUI()
//
//        verify {
//            printer.printLine(match { it.contains("There is no meals Found") })
//        }
//    }



    @Test
    fun `should display meals and interact with user when meals are available`() {
        val meals = listOf(buildMeal(1, "Salad"), buildMeal(2, "Fries"))
        every { getPotatoMealsUseCase.getRandomPotatoMeals(10) } returns Result.success(meals)
        every { searchUtils.getValidMealIndex(reader, meals.size) } returns 0
        every { searchUtils.shouldSearchAgain(reader) } returnsMany listOf(true, false)

        potatoLoversUI.showPotatoLoversUI()

        verify { printer.printLine("${Constants.I_LOVE_POTATO_HERE}10${Constants.MEAL_INCLUDE_POTATO}\n") }
        verify(atLeast = 1) { printer.printLine("\n${Constants.MEAL_DETAILS_PROMPT}") }
        verify { mealDisplayer.display(meals[0]) }
        verify(exactly = 2) { searchUtils.shouldSearchAgain(reader) }
    }

    @Test
    fun `should handle invalid meal selection gracefully`() {
        val meals = listOf(buildMeal(1, "Salad"), buildMeal(2, "Fries"))
        every { getPotatoMealsUseCase.getRandomPotatoMeals(10) } returns Result.success(meals)
        every { searchUtils.getValidMealIndex(reader, meals.size) } returns null
        every { searchUtils.shouldSearchAgain(reader) } returns false

        potatoLoversUI.showPotatoLoversUI()

        verify { printer.printLine("${Constants.I_LOVE_POTATO_HERE}10${Constants.MEAL_INCLUDE_POTATO}\n") }
        verify { printer.printLine(Constants.SKIPPING_MEAL_DETAILS) }
        verify { searchUtils.shouldSearchAgain(reader) }
    }


    @Test
    fun `should stop interaction when user chooses not to search again`() {
        val meals = listOf(buildMeal(1, "Salad"), buildMeal(2, "Fries"))
        every { getPotatoMealsUseCase.getRandomPotatoMeals(10) } returns Result.success(meals)
        every { searchUtils.getValidMealIndex(reader, meals.size) } returns 0
        every { searchUtils.shouldSearchAgain(reader) } returns false

        potatoLoversUI.showPotatoLoversUI()

        verify { printer.printLine("${Constants.I_LOVE_POTATO_HERE}10${Constants.MEAL_INCLUDE_POTATO}\n") }
        verify(atLeast = 1) { printer.printLine("\n${Constants.MEAL_DETAILS_PROMPT}") }
        verify { mealDisplayer.display(meals[0]) }
        verify { searchUtils.shouldSearchAgain(reader) }
    }

    @Test
    fun `should handle error in meal fetching`() {
        every { getPotatoMealsUseCase.getRandomPotatoMeals(10) } returns Result.failure(Exception("Error"))

        potatoLoversUI.showPotatoLoversUI()

        verify { printer.printLine(Constants.NO_MEALS_FOUND) }
    }
}
