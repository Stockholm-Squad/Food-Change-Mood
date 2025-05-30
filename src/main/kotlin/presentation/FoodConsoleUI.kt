package org.example.presentation

import org.example.input_output.input.InputReader
import org.example.presentation.features.*
import org.example.utils.MenuOption


class FoodConsoleUI(
    private val reader: InputReader,
    private val healthyFastFoodMealsUI: GetHealthyFastFoodMealsUI,
    private val exploreCountryMealsUI: ExploreCountryMealsUI,
    private val getIraqiMealsUI: GetIraqiMealsUI,
    private val guessGameUI: GuessGameUI,
    private val gymHelperUI: GymHelperUI,
    private val ingredientGameUI: IngredientGameUI,
    private val italianLargeGroupMealsUI: ItalianLargeGroupMealsUI,
    private val ketoDietMealUI: KetoDietMealUI,
    private val potatoLoversUI: PotatoLoversUI,
    private val proteinSeafoodRankingUI: ProteinSeafoodRankingUI,
    private val searchByAddDateUI: SearchByAddDateUI,
    private val searchMealByNameUI: SearchMealByNameUI,
    private val suggestEasyMealsUI: SuggestEasyMealsUI,
    private val suggestMealForSoThinPeopleUI: SuggestMealForSoThinPeopleUI,
    private val suggestSweetNoEggsUI: SuggestSweetNoEggsUI
) {

    fun start() {
        welcomeUser()

        while (true) {
            printMenu()
            if (handleEnteredMenuOption(reader.readIntOrNull())) break
        }
    }

    private fun handleEnteredMenuOption(option: Int?): Boolean {
        when (getEnteredOption(option)) {
            MenuOption.HEALTHY_FAST_FOOD -> healthyFastFoodMealsUI.showHealthyFastFoodMeals()
            MenuOption.SEARCH_BY_NAME -> searchMealByNameUI.handleSearchByName()
            MenuOption.IRAQI_MEALS -> getIraqiMealsUI.showIraqiMeals()
            MenuOption.EASY_MEALS -> suggestEasyMealsUI.showEasySuggestions()
            MenuOption.GUESS_GAME -> guessGameUI.playGuessGame()
            MenuOption.SWEETS_NO_EGGS -> suggestSweetNoEggsUI.handleSweetsNoEggs()
            MenuOption.KETO_MEALS -> ketoDietMealUI.showKetoMeal()
            MenuOption.SEARCH_BY_DATE -> searchByAddDateUI.searchMealsByDate()
            MenuOption.GYM_HELPER -> gymHelperUI.useGymHelper()
            MenuOption.COUNTRY_FOOD -> exploreCountryMealsUI.handleCountryByNameAction()
            MenuOption.INGREDIENT_GAME -> ingredientGameUI.start()
            MenuOption.POTATO_LOVERS -> potatoLoversUI.showPotatoLoversUI()
            MenuOption.HIGH_CALORIE_MEAL -> suggestMealForSoThinPeopleUI.showMealWithHighCalorie()
            MenuOption.SEAFOOD_RANKING -> proteinSeafoodRankingUI.proteinSeafoodRanking()
            MenuOption.ITALIAN_GROUP_MEALS -> italianLargeGroupMealsUI.italianLargeGroupMealsUI()
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
