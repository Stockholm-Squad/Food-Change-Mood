import io.mockk.*
import org.example.logic.usecases.GetMealByNameUseCase
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.presentation.features.SearchMealByNameUI
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `handleSearchByName shows error for empty input`() {
        // Given
        listOf("", "   ", null).forEach { input ->
            every { reader.readStringOrNull() } returns input

            // When
            searchMealByNameUI.handleSearchByName()

            // Then
            verify { printer.printLine(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY) }
        }
    }

    @Test
    fun `handleSearchByName displays multiple meals`() {
        // Given
        val meals = listOf(
            buildMeal(1, "Pizza Margherita"),
            buildMeal(2, "Pepperoni Pizza")
        )
        every { reader.readStringOrNull() } returnsMany listOf("pizza", "n")
        every { getMealByNameUseCase.getMealByName("pizza") } returns Result.success(meals)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify {
            printer.printLine(Constants.FOUND_MEALS.format(2))
            printer.printLine("1. Pizza Margherita")
            printer.printLine("2. Pepperoni Pizza")
            printer.printLine(Constants.MEAL_DETAILS_PROMPT)
        }
    }

    @Test
    fun `handleSearchByName handles no results`() {
        // Given
        every { reader.readStringOrNull() } returnsMany listOf("invalid", "n")
        every { getMealByNameUseCase.getMealByName("invalid") } returns Result.success(emptyList())

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleSearchByName shows meal details for valid selection`() {
        // Given
        val meals = listOf(
            buildMeal(1, "Caprese Pizza"),
            buildMeal(2, "BBQ Chicken Pizza")
        )
        every { reader.readStringOrNull() } returnsMany listOf("pizza", "2", "n")
        every { getMealByNameUseCase.getMealByName("pizza") } returns Result.success(meals)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify {
            printer.printLine(Constants.MEAL_DETAILS_HEADER.format("BBQ Chicken Pizza"))
            printer.printMeal(meals[1])
        }
    }

    @Test
    fun `handleSearchByName handles invalid index selection`() {
        // Given
        val meals = listOf(buildMeal(1, "Hawaiian Pizza"))
        every { reader.readStringOrNull() } returnsMany listOf("pizza", "2", "n")
        every { getMealByNameUseCase.getMealByName("pizza") } returns Result.success(meals)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }

    @Test
    fun `handleSearchByName handles non-numeric selection`() {
        // Given
        val meals = listOf(buildMeal(1, "Veggie Pizza"))
        every { reader.readStringOrNull() } returnsMany listOf("pizza", "abc", "n")
        every { getMealByNameUseCase.getMealByName("pizza") } returns Result.success(meals)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }

    @Test
    fun `handleSearchByName handles search repetition`() {
        // Given
        every { reader.readStringOrNull() } returnsMany listOf("pizza", "y", "pasta", "n")
        every { getMealByNameUseCase.getMealByName(any()) } returns Result.success(emptyList())

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(exactly = 2) { printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH) }
        verify(exactly = 2) { getMealByNameUseCase.getMealByName(any()) }
    }

    @Test
    fun `handleSearchByName handles case-insensitive inputs`() {
        // Given
        every { reader.readStringOrNull() } returnsMany listOf("PIZZA", "N")
        every { getMealByNameUseCase.getMealByName("PIZZA") } returns Result.success(emptyList())

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { getMealByNameUseCase.getMealByName("PIZZA") }
    }

    @Test
    fun `handleSearchByName handles special characters`() {
        // Given
        every { reader.readStringOrNull() } returnsMany listOf("pi@zza!", "n")
        every { getMealByNameUseCase.getMealByName("pi@zza!") } returns
                Result.failure(Throwable(Constants.NO_MEALS_FOUND_MATCHING))

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleSearchByName handles single meal case`() {
        // Given
        val meal = buildMeal(1, "Cheese Pizza")
        every { reader.readStringOrNull() } returnsMany listOf("cheese", "1", "n")
        every { getMealByNameUseCase.getMealByName("cheese") } returns Result.success(listOf(meal))

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify {
            printer.printLine("1. Cheese Pizza")
            printer.printMeal(meal)
        }
    }

    @Test
    fun `handleSearchByName() should print 'No meals found' when use case returns empty list`() {
        // Given
        val pattern = "invalid"
        every { reader.readStringOrNull() } returnsMany listOf(pattern, "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(emptyList())

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleSearchByName() should handle empty input for meal selection`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readStringOrNull() } returnsMany listOf(pattern, "", "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should handle invalid search again input`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readStringOrNull() } returnsMany listOf(pattern, "maybe", "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should handle null input during search repetition`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readStringOrNull() } returnsMany listOf(pattern, null)
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should handle empty input during search repetition`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readStringOrNull() } returnsMany listOf(pattern, "")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should handle uppercase 'Y' in search repetition`() {
        // Given
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readStringOrNull() } returnsMany listOf("Pizza", "Y", "Pizza", "n")
        every { getMealByNameUseCase.getMealByName(any()) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(atLeast = 1) { printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH) }
    }

    @Test
    fun `handleSearchByName() should proceed with valid input`() {
        // Given
        val pattern = "ValidInput"
        val mealList = listOf(buildMeal(name = "Meal", id = 1))
        every { reader.readStringOrNull() } returnsMany listOf(pattern, "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { getMealByNameUseCase.getMealByName(pattern) }
    }

    @Test
    fun `handleSearchByName() should handle use case failure with null message`() {
        // Given
        val pattern = "invalid"
        every { reader.readStringOrNull() } returnsMany listOf(pattern, "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.failure(Throwable())

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(null) }
    }

    @Test
    fun `handleSearchByName() should exit on invalid search again input`() {
        // Given
        every { reader.readStringOrNull() } returnsMany listOf("Pizza", "maybe", "n")
        every { getMealByNameUseCase.getMealByName(any()) } returns Result.success(emptyList())

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should handle null search again input`() {
        // Given
        every { reader.readStringOrNull() } returnsMany listOf("Pizza", null)
        every { getMealByNameUseCase.getMealByName(any()) } returns Result.success(emptyList())

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify { printer.printLine(Constants.GOODBYE_MESSAGE) }
    }

    @Test
    fun `handleSearchByName() should handle uppercase Y with spaces`() {
        // Given
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readStringOrNull() } returnsMany listOf("Pizza", "  Y  ", "n")
        every { getMealByNameUseCase.getMealByName(any()) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(atLeast = 1) { printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH) }
    }

    @Test
    fun `handleSearchByName() should handle mixed case and trimmed input`() {
        // Given
        val mealList = listOf(buildMeal(name = "Pizza", id = 1))
        every { reader.readStringOrNull() } returnsMany listOf("Pizza", "\t y \n", "n")
        every { getMealByNameUseCase.getMealByName(any()) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(atLeast = 1) { printer.printLine(Constants.ENTER_MEAL_KEYWORD_TO_SEARCH) }
    }

    @Test
    fun `handleSearchByName() should print 'No meals found matching' when no results`() {
        // Given
        val pattern = "invalid"
        every { reader.readStringOrNull() } returnsMany listOf(pattern, "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.failure(Throwable(Constants.NO_MEALS_FOUND_MATCHING))

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(atLeast = 1) { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    @Test
    fun `handleSearchByName() should display meals when results are found`() {
        // Given
        val pattern = "Pizza"
        val mealList = listOf(
            buildMeal(name = "pizza wrap", id = 1),
            buildMeal(name = "5 minute bread pizza", id = 2)
        )
        every { reader.readStringOrNull() } returnsMany listOf(pattern, "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(atLeast = 1) { printer.printLine(Constants.FOUND_MEALS.format(mealList.size)) }
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
        every { reader.readStringOrNull() } returnsMany listOf(pattern, userInputIndex.toString(), "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)
        every { printer.printMeal(any()) } just Runs

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(atLeast = 1) {
            printer.printLine(Constants.MEAL_DETAILS_HEADER.format(selectedMeal.name))
            printer.printMeal(selectedMeal)
        }
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
        val invalidInput = "4"
        every { reader.readStringOrNull() } returnsMany listOf(pattern, invalidInput, "n")
        every { getMealByNameUseCase.getMealByName(pattern) } returns Result.success(mealList)

        // When
        searchMealByNameUI.handleSearchByName()

        // Then
        verify(atLeast = 1) { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }

}