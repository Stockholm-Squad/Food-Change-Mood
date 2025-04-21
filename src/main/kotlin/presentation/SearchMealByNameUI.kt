package presentation

import org.example.logic.model.FoodChangeModeResults
import org.example.logic.usecases.GetMealByNameUseCase


class SearchMealByNameUI(private val getMealByNameUseCase: GetMealByNameUseCase) {

    fun handleSearchByName() {
        print("🔍 Enter a meal keyword to search: ")
        val name = readlnOrNull()?.trim()

        val result = getMealByNameUseCase.getMealByName(name)

        when (result) {
            is FoodChangeModeResults.Success -> {
                println("✅ Found ${result.model.size} meal(s) matching '$name':")
                result.model.forEach { meal ->
                    println("- ${meal.name}")
                }
            }
            is FoodChangeModeResults.Fail -> {
                println("❌ ${result.exception.message}")
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
