package org.example.logic

import model.Meal
import java.util.Collections.emptyList

class GetEasyFoodSuggestionsUseCase(
    private val mealRepository: MealRepository
) {
    fun getEasyFood():List<Meal>{
        val allMealsList = mealRepository.getAllMeals()
        val easyFoodList = allMealsList.filter { meal ->
            meal.preparationTime?.let { prepTime ->
                meal.numberOfIngredients?.let { numberOfIngredients ->
                    meal.numberOfSteps?.let { numberOfSteps ->
                        prepTime <= 30 && numberOfIngredients <= 5 && numberOfSteps <=6
                    } ?: false
                } ?: false
            } ?: false
        }.shuffled()
            .take(10)
 return if (easyFoodList.isNotEmpty()) easyFoodList else emptyList()
        }
}