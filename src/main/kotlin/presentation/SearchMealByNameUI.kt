package presentation;

import logic.SearchMealByNameUseCase

class SearchMealByNameUI(private val searchMealByNameUseCase: SearchMealByNameUseCase) {

    fun handleSearchByName() {
        print("🔍 Enter a meal keyword to search: ")
        val name = readlnOrNull()?.trim()

        if (name.isNullOrEmpty()) {
            println("⚠️ Please enter a valid meal name.")
            return
        }

        val matchingMeals = searchMealByNameUseCase.searchMealsByName(name)

        if (matchingMeals.isEmpty()) {
            println("❌ No meals found with the name '$name'.")
        } else {
            println("✅ Found ${matchingMeals.size} meal(s) matching '$name':")
            matchingMeals.forEach { meal ->
                println("- ${meal.name}")
            }
        }
        askForMoreMeals()
    }

    private fun askForMoreMeals() {
        println(
            "------------------------------------------------\n" +
                    "Would you like to search again? (y or n)"
        )
        if (readlnOrNull()?.trim()?.lowercase() == "y") {
            handleSearchByName()
        } else {
            println("Okay! Enjoy your meals! 😋")
        }
    }
}
