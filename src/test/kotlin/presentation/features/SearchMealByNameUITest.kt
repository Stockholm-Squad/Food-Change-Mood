package presentation.features

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetMealByNameUseCase
import org.example.presentation.features.SearchMealByNameUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import utils.buildMeal


class SearchMealByNameUITest {
    private lateinit var getMealByNameUseCase: GetMealByNameUseCase
    private lateinit var searchMealByNameUI: SearchMealByNameUI
    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        reader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        getMealByNameUseCase = mockk()
        searchMealByNameUI = SearchMealByNameUI(getMealByNameUseCase, reader, printer)
    }

    @Test
    fun `handleSearchByName() should print 'Search query cannot be empty' when pattern is empty`() {
        // Given
        val pattern = ""
        every { reader.readLineOrNull() } returns pattern

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(exactly = 1) { printer.printLine(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY) }
    }

    @Test
    fun `handleSearchByName() should print 'No meals found matching' when no results`() {
        // Given
        val pattern = "invalid"
        every { reader.readLineOrNull() } returns pattern
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.failure(Throwable(Constants.NO_MEALS_FOUND_MATCHING))

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(exactly = 1) { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleSearchByName() should display meals when results are found`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(
            buildMeal(name = "pizza wrap", id = 1),
            buildMeal(name = "5 minute bread pizza", id = 2),
        )
        every { reader.readLineOrNull() } returnsMany listOf(pattern)
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.FOUND_MEALS.format(mealList.size)) }
    }

    @Test
    fun `handleSearchByName() should show meal details when user enters a valid number`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(
            buildMeal(name = "pizza wrap", id = 1),
            buildMeal(name = "5 minute bread pizza", id = 2),
            buildMeal(name = "vegan pizza", id = 3)
        )

        val userInputIndex = 2
        val selectedMeal = mealList[userInputIndex - 1]

        every { reader.readLineOrNull() } returnsMany listOf(pattern, userInputIndex.toString())
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)
        every { printer.printMeal(any()) } just Runs

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.MEAL_DETAILS_HEADER.format(selectedMeal.name)) }
        verify { printer.printMeal(selectedMeal) }
    }

    @Test
    fun `handleSearchByName() should print invalid selection when user enters an out-of-range number`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(
            buildMeal(name = "pizza wrap", id = 1),
            buildMeal(name = "5 minute bread pizza", id = 2),
            buildMeal(name = "vegan pizza", id = 3)
        )

        val invalidInput = "-1"

        every { reader.readLineOrNull() } returnsMany listOf(pattern, invalidInput)
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)
        every { printer.printMeal(any()) } just Runs

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should print invalid message when user enters non-numeric input for meal selection`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(
            buildMeal(name = "pizza wrap", id = 1),
            buildMeal(name = "5 minute bread pizza", id = 2),
            buildMeal(name = "vegan pizza", id = 3)
        )

        every { reader.readLineOrNull() } returnsMany listOf(pattern, "abc")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should print invalid message when user enters number larger than meal list size`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(
            buildMeal(name = "pizza wrap", id = 1),
            buildMeal(name = "5 minute bread pizza", id = 2),
            buildMeal(name = "vegan pizza", id = 3)
        )
        val invalidInput = "4"

        every { reader.readLineOrNull() } returnsMany listOf(pattern, invalidInput)
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should print goodbye message when user enters non-y when asked to search again`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readLineOrNull() } returnsMany listOf(pattern, "n", "x")

        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should skip meal details when user inputs 'n'`() {
        // Given
        val meals = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readLineOrNull() } returnsMany listOf("Pizza", "n", "n")
        every { getMealByNameUseCase.getMealByName("Pizza") } returns Result.success(meals)
        every { printer.printMeal(any()) } just Runs

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(exactly = 0) { printer.printMeal(any()) }
    }

    @Test
    fun `handleSearchByName() should restart search when user inputs 'y' after viewing meals`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))

        every { reader.readLineOrNull() } returnsMany listOf(pattern, "y", pattern, "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(exactly = 1) { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }



    @Test
    fun `handleSearchByName() should print goodbye message when user does not want to search again`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readLineOrNull() } returnsMany listOf(pattern, "n")

        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

}
