package presentation.features
import io.mockk.*
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealByNameUseCase
import org.example.presentation.features.SearchMealByNameUI
import org.example.presentation.features.utils.ConsoleMealDisplayer
import org.example.presentation.features.utils.SearchUtils
import org.example.utils.Constants
import utils.buildMeal
import kotlin.test.BeforeTest
import kotlin.test.Test

class SearchMealByNameUITest {

    private lateinit var getMealByNameUseCase: GetMealByNameUseCase
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter
    private lateinit var mealDisplayer: ConsoleMealDisplayer
    private lateinit var searchUtils: SearchUtils
    private lateinit var searchMealByNameUI: SearchMealByNameUI

    @BeforeTest
    fun setup() {
        getMealByNameUseCase = mockk()
        reader = mockk()
        printer = mockk(relaxed = true)
        mealDisplayer = mockk(relaxed = true)
        searchUtils = mockk()
        searchMealByNameUI = SearchMealByNameUI(
            getMealByNameUseCase,
            reader,
            printer,
            mealDisplayer,
            searchUtils
        )
    }

    @Test
    fun `handleSearchByName() prints error when input is blank`() {
        // Given
        every { searchUtils.readNonBlankTrimmedInput(reader) } returns null
        every { searchUtils.shouldSearchAgain(reader) } returns null

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY) }
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() displays no meals found if result is empty`() {
        // Given
        every { searchUtils.readNonBlankTrimmedInput(reader) } returns "salad"
        every { getMealByNameUseCase.getMealByName("salad") } returns Result.success(emptyList())
        every { searchUtils.shouldSearchAgain(reader) } returns null

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleSearchByName() displays meal list and handles invalid selection`() {
        // Given
        val meals = listOf(buildMeal(1, "Salad"))
        every { searchUtils.readNonBlankTrimmedInput(reader) } returns "salad"
        every { getMealByNameUseCase.getMealByName("salad") } returns Result.success(meals)
        every { searchUtils.getValidMealIndex(reader, meals.size) } returns null
        every { searchUtils.shouldSearchAgain(reader) } returns null

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.FOUND_MEALS.format(1)) }
        verify { printer.printLine("1. Salad") }
        verify { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() displays meal list and shows meal details on valid selection`() {
        // Given
        val meals = listOf(buildMeal(1, "Salad"))
        every { searchUtils.readNonBlankTrimmedInput(reader) } returns "salad"
        every { getMealByNameUseCase.getMealByName("salad") } returns Result.success(meals)
        every { searchUtils.getValidMealIndex(reader, meals.size) } returns 0
        every { searchUtils.shouldSearchAgain(reader) } returns null

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { mealDisplayer.display(meals[0]) }
    }

    @Test
    fun `handleSearchByName() prints exception message when getMealByName fails`() {
        // Given
        val exception = RuntimeException("Something went wrong")
        every { searchUtils.readNonBlankTrimmedInput(reader) } returns "pizza"
        every { getMealByNameUseCase.getMealByName("pizza") } returns Result.failure(exception)
        every { searchUtils.shouldSearchAgain(reader) } returns null

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine("Something went wrong") }
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() restarts search if user chooses to search again`() {
        // Given
        val meals = listOf(buildMeal(1, "Burger"))

        every { searchUtils.readNonBlankTrimmedInput(reader) } returnsMany listOf("burger", "burger")
        every { getMealByNameUseCase.getMealByName("burger") } returnsMany listOf(
            Result.success(meals),
            Result.success(emptyList())
        )
        every { searchUtils.getValidMealIndex(reader, meals.size) } returns null
        every { searchUtils.shouldSearchAgain(reader) } returnsMany listOf(true, null)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(exactly = 2) { getMealByNameUseCase.getMealByName("burger") }
        verify { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleSearchByName() prints exception message when use case fails`() {
        // Given
        val exception = RuntimeException("Something went wrong")
        every { searchUtils.readNonBlankTrimmedInput(reader) } returns "pasta"
        every { getMealByNameUseCase.getMealByName("pasta") } returns Result.failure(exception)
        every { searchUtils.shouldSearchAgain(reader) } returns null

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verifySequence {
            printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH)
            searchUtils.readNonBlankTrimmedInput(reader)
            getMealByNameUseCase.getMealByName("pasta")
            printer.printLine("Something went wrong")
            printer.printLine(Constants.SEARCH_AGAIN_PROMPT)
            searchUtils.shouldSearchAgain(reader)
            printer.printLine(Constants.GOODBYE_MESSAGE)
        }
    }

    @Test
    fun `handleSearchByName() should call itself again when user wants to search again`() {
        // Given
        every { searchUtils.readNonBlankTrimmedInput(reader) } returns "pasta" andThen "burger"
        every { getMealByNameUseCase.getMealByName("pasta") } returns Result.success(emptyList())
        every { getMealByNameUseCase.getMealByName("burger") } returns Result.success(emptyList())
        every { searchUtils.shouldSearchAgain(reader) } returns true andThen null

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verifySequence {
            printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH)
            searchUtils.readNonBlankTrimmedInput(reader)
            getMealByNameUseCase.getMealByName("pasta")
            printer.printLine(Constants.NO_MEALS_FOUND_MATCHING)

            printer.printLine(Constants.SEARCH_AGAIN_PROMPT)
            searchUtils.shouldSearchAgain(reader)

            printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH)
            searchUtils.readNonBlankTrimmedInput(reader)
            getMealByNameUseCase.getMealByName("burger")
            printer.printLine(Constants.NO_MEALS_FOUND_MATCHING)

            printer.printLine(Constants.SEARCH_AGAIN_PROMPT)
            searchUtils.shouldSearchAgain(reader)
            printer.printLine(Constants.GOODBYE_MESSAGE)
        }
    }
}
