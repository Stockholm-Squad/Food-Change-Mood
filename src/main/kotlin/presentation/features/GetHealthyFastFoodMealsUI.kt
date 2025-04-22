package org.example.presentation.features

import org.example.logic.usecases.GetHealthyFastFoodUseCase

class GetHealthyFastFoodMealsUI(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase
) {

    fun showHealthyFastFoodMeals() {
        getHealthyFastFoodUseCase.getHealthyFastFood().fold(
            onSuccess = { allMeals ->
                allMeals.forEachIndexed { index, meal ->
                    println(
                        "Meal ${index + 1}:\n" +
                                "Name='${meal.name}'\n" +
                                "ID=${meal.id}\n" +
                                "Minutes=${meal.minutes}\n" +
                                "ContributorID=${meal.contributorId}\n" +
                                "Submitted='${meal.submitted}'\n" +
                                "Tags=${meal.tags}\n" +
                                "Nutrition=${meal.nutrition}\n" +
                                "StepsCount=${meal.numberOfSteps}\n" +
                                "Steps=${meal.steps}\n" +
                                "Description='${meal.description?.take(30)}...'\n" +
                                "Ingredients=${meal.ingredients}\n" +
                                "IngredientsCount=${meal.numberOfIngredients}\n"
                    )
                }
            },
            onFailure = { error ->
                println("❌ Failed to load meals: ${error.message}")
            }
        )
    }

}