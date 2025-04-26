package presentation

import io.mockk.*
import org.example.input_output.input.InputReader
import org.example.presentation.FoodConsoleUI
import org.example.presentation.features.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

class FoodConsoleUITest {
    private lateinit var reader: InputReader
    private lateinit var healthyFastFoodMealsUI: GetHealthyFastFoodMealsUI
    private lateinit var exploreCountryMealsUI: ExploreCountryMealsUI
    private lateinit var getIraqiMealsUI: GetIraqiMealsUI
    private lateinit var guessGameUI: GuessGameUI
    private lateinit var gymHelperUI: GymHelperUI
    private lateinit var ingredientGameUI: IngredientGameUI
    private lateinit var italianLargeGroupMealsUI: ItalianLargeGroupMealsUI
    private lateinit var ketoDietMealUI: KetoDietMealUI
    private lateinit var potatoLoversUI: PotatoLoversUI
    private lateinit var proteinSeafoodRankingUI: ProteinSeafoodRankingUI
    private lateinit var searchByAddDateUI: SearchByAddDateUI
    private lateinit var searchMealByNameUI: SearchMealByNameUI
    private lateinit var suggestEasyMealsUI: SuggestEasyMealsUI
    private lateinit var suggestMealForSoThinPeopleUI: SuggestMealForSoThinPeopleUI
    private lateinit var suggestSweetNoEggsUI: SuggestSweetNoEggsUI
    private lateinit var foodConsoleUI: FoodConsoleUI

    @BeforeEach
    fun setUp() {
        reader = mockk(relaxed = true)
        healthyFastFoodMealsUI = mockk(relaxed = true)
        exploreCountryMealsUI = mockk(relaxed = true)
        getIraqiMealsUI = mockk(relaxed = true)
        guessGameUI = mockk(relaxed = true)
        gymHelperUI = mockk(relaxed = true)
        ingredientGameUI = mockk(relaxed = true)
        italianLargeGroupMealsUI = mockk(relaxed = true)
        ketoDietMealUI = mockk(relaxed = true)
        potatoLoversUI = mockk(relaxed = true)
        proteinSeafoodRankingUI = mockk(relaxed = true)
        searchByAddDateUI = mockk(relaxed = true)
        searchMealByNameUI = mockk(relaxed = true)
        suggestEasyMealsUI = mockk(relaxed = true)
        suggestMealForSoThinPeopleUI = mockk(relaxed = true)
        suggestSweetNoEggsUI = mockk(relaxed = true)

        foodConsoleUI = FoodConsoleUI(
            healthyFastFoodMealsUI,
            exploreCountryMealsUI,
            getIraqiMealsUI,
            guessGameUI,
            gymHelperUI,
            ingredientGameUI,
            italianLargeGroupMealsUI,
            ketoDietMealUI,
            potatoLoversUI,
            proteinSeafoodRankingUI,
            searchByAddDateUI,
            searchMealByNameUI,
            suggestEasyMealsUI,
            suggestMealForSoThinPeopleUI,
            suggestSweetNoEggsUI
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @ParameterizedTest
    @CsvSource(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"
    )
    fun `start() triggers correct feature based on menu option`(optionInput: Int) {
        every { reader.readIntOrNull() } returns optionInput andThen 0

        foodConsoleUI.start()

        verify {
            when (optionInput) {
                1 -> healthyFastFoodMealsUI.showHealthyFastFoodMeals()
                2 -> searchMealByNameUI.handleSearchByName()
                3 -> getIraqiMealsUI.showIraqiMeals()
                4 -> suggestEasyMealsUI.showEasySuggestions()
                5 -> guessGameUI.playGuessGame()
                6 -> suggestSweetNoEggsUI.handleSweetsNoEggs()
                7 -> ketoDietMealUI.showKetoMeal()
                8 -> searchByAddDateUI.searchMealsByDate()
                9 -> gymHelperUI.useGymHelper()
                10 -> exploreCountryMealsUI.handleCountryByNameAction()
                11 -> ingredientGameUI.start()
                12 -> potatoLoversUI.showPotatoLoversUI()
                13 -> suggestMealForSoThinPeopleUI.showMealWithHighCalorie()
                14 -> proteinSeafoodRankingUI.proteinSeafoodRanking()
                15 -> italianLargeGroupMealsUI.italianLargeGroupMealsUI()
            }
        }
    }

    @Test
    fun `start() exits immediately when Exit (0) is chosen`() {
        every { reader.readIntOrNull() } returns 0

        foodConsoleUI.start()

        confirmVerified(
            healthyFastFoodMealsUI,
            exploreCountryMealsUI,
            getIraqiMealsUI,
            guessGameUI,
            gymHelperUI,
            ingredientGameUI,
            italianLargeGroupMealsUI,
            ketoDietMealUI,
            potatoLoversUI,
            proteinSeafoodRankingUI,
            searchByAddDateUI,
            searchMealByNameUI,
            suggestEasyMealsUI,
            suggestMealForSoThinPeopleUI,
            suggestSweetNoEggsUI
        )
    }

    @Test
    fun `start() handles invalid numeric input gracefully`() {
        every { reader.readIntOrNull() } returns 999 andThen 1 andThen 0

        foodConsoleUI.start()

        verify { healthyFastFoodMealsUI.showHealthyFastFoodMeals() }
    }

    @Test
    fun `start() handles non-numeric input gracefully`() {
        every { reader.readIntOrNull() } returns null andThen 2 andThen 0

        foodConsoleUI.start()

        verify { searchMealByNameUI.handleSearchByName() }
    }

    @Test
    fun `start() handles null input gracefully`() {
        every { reader.readIntOrNull() } returns null andThen 0

        foodConsoleUI.start()
    }
}