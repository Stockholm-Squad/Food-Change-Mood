package org.example.presentation

import presentation.*

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
            when (readln().toIntOrNull()) {
                1 -> healthyFastFood.showHealthyFastFoodMeals()
                2 -> searchByName.handleSearchByName()
                3 -> iraqiMeals.getIraqiMeals()
                4 -> easyMeals.showEasySuggestions()
                5 -> guessGame.playGuessGame()
                6 -> sweetNoEggs.showSweetsNoEggs()
                7 -> ketoMeals.showKetoMeal()
                8 -> searchByDate.searchMealsByDate()
                9 -> gymHelper.useGymHelper()
                10 -> countryFood.exploreCountryFoodCulture()
                11 -> ingredientGame.ingredientGameUI()
                12 -> potatoLovers.potatoLoversUI()
                13 -> highCalorieMeal.highCalorieMealUI()
                14 -> seafoodRanking.proteinSeafoodRanking()
                15 -> italianForGroups.italianLargeGroupMealsUI()
                0 -> {
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
