package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetCountryMealsUseCase
import org.example.presentation.features.ExploreCountryMealsUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import kotlin.test.Test

class ExploreCountryMealsUITest {


    private lateinit var getCountryMealsUseCase: GetCountryMealsUseCase
    private lateinit var exploreCountryMealsUI: ExploreCountryMealsUI
    private lateinit var stringReader: InputReader
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        stringReader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getCountryMealsUseCase = mockk(relaxed = true)
        exploreCountryMealsUI = ExploreCountryMealsUI(getCountryMealsUseCase, stringReader, printer)

    }

    @Test
    fun `handleCountryByNameAction() should return a list of chosen meals`() {
        //Given
        val countryName = "asian"
        val meal1 = buildMeal(
            id = 1, name = "emotional balance  spice mixture", tags = listOf("asian", "indian")
        )
        val meal2 = buildMeal(
            id = 2, name = "i stole the idea from mirj  sesame noodles", tags = listOf("asian")
        )
        val meal3 = buildMeal(
            id = 3, tags = listOf("asian")
        )
        every { stringReader.readStringOrNull() } returns countryName
        every { getCountryMealsUseCase.getMealsForCountry(countryName) } returns Result.success(
            listOf(meal1, meal2, meal3)
        )
        //When
        exploreCountryMealsUI.handleCountryByNameAction()
        //Then

        verify { printer.printLine(Constants.HERE_ARE_THE_MEALS) }
    }

    @Test
    fun `handleCountryByNameAction() should return Invalid Input when empty input entered`() {
        //Given
        val countryName = ""
        val meal1 = buildMeal(
            id = 1, name = "emotional balance  spice mixture", tags = listOf("asian", "indian")
        )
        val meal2 = buildMeal(
            id = 2, name = "i stole the idea from mirj  sesame noodles", tags = listOf("asian")
        )
        val meal3 = buildMeal(
            id = 3, tags = listOf("asian")
        )
        every { stringReader.readStringOrNull() } returns countryName
        every { getCountryMealsUseCase.getMealsForCountry(countryName) } returns Result.success(
            listOf(meal1, meal2, meal3)
        )
        //When
        exploreCountryMealsUI.handleCountryByNameAction()
        //Then

        verify { printer.printLine(Constants.INVALID_INPUT) }
    }

    @Test
    fun `handleCountryByNameAction() should return Invalid Input when null input`() {
        //Given
        val countryName = null

        every { stringReader.readStringOrNull() } returns countryName
        //When
        exploreCountryMealsUI.handleCountryByNameAction()
        //Then

        verify { printer.printLine(Constants.INVALID_INPUT) }
    }

    @Test
    fun `handleCountryByNameAction() should return Invalid Input for wrong input meals`() {
        //Given
        val countryName = "asdasdasd"
        every { stringReader.readStringOrNull() } returns countryName
        every { getCountryMealsUseCase.getMealsForCountry(countryName) } returns Result.failure(
            NoSuchElementException(
                Constants.INVALID_INPUT
            )
        )
        //When
        exploreCountryMealsUI.handleCountryByNameAction()
        //Then

        verify { printer.printLine(Constants.INVALID_INPUT) }
    }

    @Test
    fun `handleCountryByNameAction() should handle case-insensitive country input`() {
        // Given
        val countryName = "AsIaN"
        val meal1 = buildMeal(
            id = 1, name = "emotional balance spice mixture", tags = listOf("asian", "indian")
        )
        val meal2 = buildMeal(
            id = 2, name = "i stole the idea from mirj sesame noodles", tags = listOf("asian")
        )

        every { stringReader.readStringOrNull() } returns countryName
        every { getCountryMealsUseCase.getMealsForCountry(countryName) } returns Result.success(
            listOf(meal1, meal2)
        )

        // When
        exploreCountryMealsUI.handleCountryByNameAction()

        // Then
        verify { printer.printLine(Constants.HERE_ARE_THE_MEALS) }
    }

    @Test
    fun `handleCountryByNameAction() should handle meals with missing optional fields`() {
        //Given
        val countryName = "asian"

        every { stringReader.readStringOrNull() } returns countryName
        every { getCountryMealsUseCase.getMealsForCountry(countryName) } returns Result.failure(
            NoSuchElementException(
                Constants.INVALID_INPUT
            )
        )
        //When
        exploreCountryMealsUI.handleCountryByNameAction()
        //Then
        verify { printer.printLine(Constants.INVALID_INPUT) }
    }


    @Test
    fun `handleCountryByNameAction() should return No meals when found when list is empty`() {
        //Given

        val meal1 = buildMeal(
            id = 1, name = null
        )
        val meal2 = buildMeal(
            id = 2
        )

        every { stringReader.readStringOrNull() } returns "asia"
        every { getCountryMealsUseCase.getMealsForCountry("asia") } returns Result.success(
            listOf()
        )
        //When
        exploreCountryMealsUI.handleCountryByNameAction()
        //Then

        verify { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleCountryByNameAction() should return  meals when found when other variables is null`() {
        //Given
        val countryName = "asia"
        val meal1 = buildMeal(
            id = 1, name = null, tags = listOf("asia"), description = null
        )
        val meal2 = buildMeal(
            id = 2,
            tags = listOf("asia"),
            ingredients = null
        )

        every { stringReader.readStringOrNull() } returns countryName
        every { getCountryMealsUseCase.getMealsForCountry(countryName) } returns Result.success(
            listOf(meal1, meal2)
        )
        //When
        exploreCountryMealsUI.handleCountryByNameAction()
        //Then

        verify { printer.printLine(match { it.contains("Meal Ingredients: null") }) }
        verify { printer.printLine(match { it.contains("Meal Description: null") }) }
        verify { printer.printLine(match { it.contains("Meal Preparation Steps: null") }) }

    }
}