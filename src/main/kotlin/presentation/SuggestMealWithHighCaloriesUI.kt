package org.example.presentation
import model.Meal
import org.example.logic.usecases.GetMealsForSoThinProblemUseCase


class SuggestMealForSoThinPeopleUI(
    private val getMealsForSoThinProblemUseCase: GetMealsForSoThinProblemUseCase,
) {
    fun getMaleWithHighCalorie() {
        println("-------------------------------------------------------------------------------------------------")
        println("🔥 Feeling too thin? This meal above 700 calories!")
        val suggestMeal = getMealsForSoThinProblemUseCase.suggestRandomMealForSoThinPeople()
        println("------------------------------------------------------------------------------------------------")
        println("Name: " + suggestMeal.name)
        println("Description: " + suggestMeal.description)
        println("--------------------------------------------------------------------------------------------------")
        println("Do you like it? (yes/no) 😊")
        when(readlnOrNull()?.trim()?.lowercase()) {
            "no" ->  getMaleWithHighCalorie()
            "yes" -> displayFullDetails(suggestMeal)
            else -> println("Invalid input! Expected yes or no.")
        }
    }
    private fun displayFullDetails(meal:Meal){
        println("Meal Name: "+meal.name)
        println("Meal Description: "+meal.description)
        println("Meal Preparation Time: "+meal.minutes +" minutes")
        println("Meal "+meal.nutrition)

    }
}
