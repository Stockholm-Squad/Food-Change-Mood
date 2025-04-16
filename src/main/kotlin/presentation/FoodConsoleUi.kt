package presentation;

import org.example.logic.usecase.GetHealthFastFoodUseCase

class FoodConsoleUi(
    private val getHealthFastFoodUseCase: GetHealthFastFoodUseCase
) {

    fun start() {
        showWelcome()
        presentFeature()
    }

    private fun presentFeature() {
        showOption()
        val input = getUserInput()
        when (input) {
            1 -> printFirst3Meals()
            else -> {
                println("Invalid Input")
            }
        }
        presentFeature()
    }

    private fun getUserInput(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

    private fun showOption() {
        println("\n=== please enter one of the following numbers ===")
        println("1 - Healthy Fast Food")
        println("2 - Search Meals by Name")
        println("3 - Identify Iraqi Meals")
        println("4 - Easy Food Suggestion")
        println("5 - Guess Game – Preparation Time ")
        println("6- Sweets with No Eggs (For users allergic to eggs)")
        println("7 - Keto Diet Meal Helper")
        println("8 - Search by Add Date")
        println("9 - Gym Helper")
        println("10 - Food Culture Explorer (Food Culture)")
        println("11 - Ingredient Game")
        println("12 - I Love Potato (Random)")
        println("13 - So Thin Problem")
        println("14 - Protein Seafood Ranking")
        println("15 - Italian Group Meals")
        print("here : ")
    }

    private fun showWelcome() {
        println("Welcome to food change mood app")
    }

    fun printFirst3Meals() {
       getHealthFastFoodUseCase.getHealthyFastFoodMeals().forEachIndexed { index, meal ->
            println(
                "Meal ${index + 1}: " +
                        "Name='${meal.name}'\n " +
                        "ID=${meal.id}\n " +
                        "Minutes=${meal.preparationTime}\n " +
                        "ContributorID=${meal.contributorId}\n " +
                        "Submitted='${meal.addedDate}\n, " +
                        "Tags=${meal.tags}\n " +
                        "Nutrition=${meal.nutrition}\n " +
                        "StepsCount=${meal.numberOfSteps}\n " +
                        "Steps=${meal.steps}\n " +
                        "Description='${meal.description.take(30)}...'\n " + // to avoid long prints
                        "Ingredients=${meal.ingredients}\n " +
                        "IngredientsCount=${meal.numberOfIngredients}\n\n"
            )
        }
    }

}
