package presentation

import org.example.presentation.*


class FoodConsoleUI() {

    fun start() {
        welcomeUser()

        while (true) {
            printMenu()
            when (readln().toIntOrNull()) {
                1 -> GetHealthyFastFoodMealsUI()
                2 -> SearchMealByNameUI()
                3 -> GetIraqiMealsUI()
                4 -> SuggestEasyMealsUI()
                5 -> GuessGameUI()
                6 -> SuggestSweetNoEggsUI()
                7 -> KetoDietMealUI()
                8 -> SearchByAddDateUI()
                9 -> GymHelperUI()
                10 -> ExploreCountryFoodUI()
                11 -> IngredientGameUI()
                12 -> PotatoLoversUI()
                13 -> HighCalorieMealUI()
                14 -> ProteinSeafoodRankingUI()
                15 -> ItalianLargeGroupMealsUI()
                0 -> {
                    println("👋 Goodbye foodie friend! Stay delicious! 🍕💖")
                    break
                }

                else -> println("😕 Oops! That’s not on the menu. Pick a number between 0 and 10!")
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
