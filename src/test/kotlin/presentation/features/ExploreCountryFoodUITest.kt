package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetCountryMealsUseCase
import org.example.presentation.features.ExploreCountryFoodUI
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.io.StringWriter
import kotlin.test.Test

class ExploreCountryFoodUITest {


    private lateinit var getCountryMealsUseCase: GetCountryMealsUseCase
    private lateinit var exploreCountryFoodUI: ExploreCountryFoodUI
    private lateinit var stringReader: InputReader<String>
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        stringReader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getCountryMealsUseCase = mockk(relaxed = true)
        exploreCountryFoodUI = ExploreCountryFoodUI(getCountryMealsUseCase,stringReader,printer)
        exploreCountryFoodUI.exploreCountryFoodCulture()
    }

    @Test
    fun `exploreCountryFoodCulture() should return a list of asian meals`() {
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
        every { stringReader.read() } returns countryName
        every { getCountryMealsUseCase.getMealsForCountry(countryName) } returns Result.success(
            listOf(meal1, meal2, meal3)
        )
        //When
        exploreCountryFoodUI.exploreCountryFoodCulture()
        //Then

        verify { printer.printLine(listOf(meal1, meal2, meal3).toString()) }
    }

}