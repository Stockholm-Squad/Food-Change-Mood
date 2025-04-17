package org.example.presentation

import data.FoodCsvParser
import data.FoodCsvReader
import org.example.data.FoodCsvRepository
import org.example.logic.MealsRepository
import org.example.model.MenuOption
import presentation.*
import java.io.File

class FoodConsoleUI {
    private val healthyFastFood = GetHealthyFastFoodMealsUI()
    private val searchByName = SearchMealByNameUI()
    private val iraqiMeals = GetIraqiMealsUI()
    private val easyMeals = SuggestEasyMealsUI()
    private val guessGame = GuessGameUI()
    private val sweetNoEggs = SuggestSweetNoEggsUI()
    private val ketoMeals = KetoDietMealUI()
    private val searchByDate = SearchByAddDateUI()
    private val gymHelper = GymHelperUI()
    private val countryFood = ExploreCountryFoodUI()
    private val ingredientGame = IngredientGameUI()
    private val potatoLovers = PotatoLoversUI()
    private val highCalorieMeal = HighCalorieMealUI()
    private val seafoodRanking = ProteinSeafoodRankingUI()
    private val italianForGroups = ItalianLargeGroupMealsUI()

    fun start() {
        welcomeUser()

        while (true) {
            printMenu()
            val option = readln().toInt()

            // Matching the input to the MenuOption enum
            when (MenuOption.entries.find { it.option == option }) {
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
                MenuOption.INGREDIENT_GAME -> ingredientGame.ingredientGameUI()
                MenuOption.POTATO_LOVERS -> potatoLovers.potatoLoversUI()
                MenuOption.HIGH_CALORIE_MEAL -> highCalorieMeal.highCalorieMealUI()
                MenuOption.SEAFOOD_RANKING -> seafoodRanking.proteinSeafoodRanking()
                MenuOption.ITALIAN_GROUP_MEALS -> italianForGroups.italianLargeGroupMealsUI()
                MenuOption.EXIT -> {
                    println("👋 Goodbye foodie friend! Stay delicious! 🍕💖")
                    break
                }

                else -> println("😕 Oops! That’s not on the menu. Pick a number between 0 and 15!")
            }
        }
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
