package presentation;

import logic.GetHealthFastFoodUseCase

class FoodConsoleUi(
    private val getHealthFastFoodUseCase: GetHealthFastFoodUseCase
) {

    fun start(){
        showWelcome()
        presentFeature()
    }

    private fun launchHealthFastFood() {
         getUserInput()?.let {
             getHealthFastFoodUseCase.getHealthyFastFood().forEach {
                 println(it)
             }
         }?:println("Invalid Input")
    }

    private fun presentFeature() {
        showOption()
        val input=getUserInput()
        when(input){
            1-> launchHealthFastFood()
            else -> { println("Invalid Input") }
        }
        presentFeature()
    }

    private fun getUserInput():Int? {
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


}
