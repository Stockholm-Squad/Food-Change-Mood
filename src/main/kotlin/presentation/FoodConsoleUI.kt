package org.example.presentation

import logic.GymHelperUseCase
import IngredientGameUseCase
import logic.SearchMealByNameUseCase
import logic.GetHealthFastFoodUseCase
import logic.GetIraqiMealsUseCase
import org.example.logic.GetSeaFoodByProteinRankUseCase
import org.example.logic.*
import org.example.logic.GetEasyFoodSuggestionsUseCase
import org.example.model.MenuOption
import presentation.*

class FoodConsoleUI(
    private val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase,
    private val sweetNoEggsUseCase: GetSweetWithNoEggsUseCase,
    private val getCountriesFoodUseCase: GetCountriesFoodUseCase,
    private val getPotatoMealsUseCase: GetPotatoMealsUseCase,
    private val gymHelperUseCase: GymHelperUseCase,
    private val ingredientGameUseCase: IngredientGameUseCase,
    private val getHealthFastFoodUseCase: GetHealthFastFoodUseCase,
    private val getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase,
    private val searchByAddDateUseCase: SearchByAddDateUseCase,
    private val italianMealsForLargeGroupUseCase: ItalianMealsForLargeGroupUseCase,
    private val searchMealByNameUseCase: SearchMealByNameUseCase,
    private val soThinProblem: SoThinProblem,
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val getGuessGameUseCase: GetGuessGameUseCase
) {
    private val healthyFastFood = GetHealthyFastFoodMealsUI(getHealthFastFoodUseCase)
    private val searchByName = SearchMealByNameUI(searchMealByNameUseCase)
    private val iraqiMeals = GetIraqiMealsUI(getIraqiMealsUseCase)
    private val easyMeals = SuggestEasyMealsUI(getEasyFoodSuggestionsUseCase)
    private val guessGame = GuessGameUI(getGuessGameUseCase)
    private val sweetNoEggs = SuggestSweetNoEggsUI(sweetNoEggsUseCase)
    private val ketoMeals = KetoDietMealUI()
    private val searchByDate = SearchByAddDateUI(searchByAddDateUseCase)
    private val gymHelper = GymHelperUI(gymHelperUseCase)
    private val countryFood = ExploreCountryFoodUI(getCountriesFoodUseCase)
    private val ingredientGame = IngredientGameUI(ingredientGameUseCase)
    private val potatoLovers = PotatoLoversUI(getPotatoMealsUseCase)
    private val highCalorieMeal = SuggestMealWithHighCaloriesUI(soThinProblem)
    private val seafoodRanking = ProteinSeafoodRankingUI(getSeaFoodByProteinRankUseCase)
    private val italianForGroups = ItalianLargeGroupMealsUI(italianMealsForLargeGroupUseCase)

    fun start() {
        welcomeUser()

        while (true) {
            printMenu()
            val enteredOption = readlnOrNull()?.toIntOrNull()
            if (handleEnteredMenuOption(enteredOption)) break
        }
    }

    private fun handleEnteredMenuOption(option: Int?): Boolean {
        when (getEnteredOption(option)) {
            MenuOption.HEALTHY_FAST_FOOD -> healthyFastFood.showHealthyFastFoodMeals()
            MenuOption.SEARCH_BY_NAME -> searchByName.handleSearchByName()
            MenuOption.IRAQI_MEALS -> iraqiMeals.getIraqiMeals()
            MenuOption.EASY_MEALS -> easyMeals.showEasySuggestions()
            MenuOption.GUESS_GAME -> guessGame.playGuessGame()
            MenuOption.SWEETS_NO_EGGS -> sweetNoEggs.showSweetsNoEggs()
            MenuOption.KETO_MEALS -> ketoMeals.showKetoMeal()
            MenuOption.SEARCH_BY_DATE -> searchByDate.searchMealsByDate()
            MenuOption.GYM_HELPER -> gymHelper.useGymHelper()
            MenuOption.COUNTRY_FOOD -> countryFood.exploreCountryFoodCulture()
            MenuOption.INGREDIENT_GAME -> ingredientGame.start()
            MenuOption.POTATO_LOVERS -> potatoLovers.showPotatoLoversUI()
            MenuOption.HIGH_CALORIE_MEAL -> highCalorieMeal.getMaleWithHighCalorie()
            MenuOption.SEAFOOD_RANKING -> seafoodRanking.proteinSeafoodRanking()
            MenuOption.ITALIAN_GROUP_MEALS -> italianForGroups.italianLargeGroupMealsUI()
            MenuOption.EXIT -> {
                exit()
                return true
            }

            else -> invalidChoice()
        }
        return false
    }

    private fun getEnteredOption(option: Int?) = MenuOption.entries.find { it.option == option }

    private fun invalidChoice() {
        println("😕 Oops! That’s not on the menu. Pick a number between 0 and 15!")
    }

    private fun exit() {
        println("👋 Goodbye foodie friend! Stay delicious! 🍕💖")
    }

    private fun welcomeUser() {
        println(
            """
            ------------------------------ 🌮 Food Change Mood App 🍔 ------------------------------
            🎉 Welcome to your ultimate foodie adventure! 🎉
            Get ready to explore delicious meals, play fun games, and discover food from all over the world! 🌍
        """.trimIndent()
        )
    }

    private fun printMenu() {
        println(
            """
            ========================= 🍽️ MENU TIME =========================
            1. Healthy Fast Food Meals 🥗
            2. Search Meal by Name 🔍
            3. Do you love Iraqi food? 🍽️
            4. Easy Meal Suggestions ⏱️
            5. The Guess Game 🎮
            6. Sweets with No Eggs 🍪
            7. Keto Diet Meal Helper 🥑
            8. Search Meals by Add Date 📅
            9. Gym Helper 💪
            10. Explore Other Countries' Food Culture 🌍
            11. Ingredient Game 🧠
            12. I Love Potato 🥔
            13. So Thin Problem 🍽️
            14. Seafood Protein Rank 🦐
            15. Italian Large Group Helper 🇮🇹
            0. Exit (Don't gooo! 😢)
            -----------------------------------------------------
            Choose an option: 
        """.trimIndent()
        )
    }
}
