package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetHealthyFastFoodUseCase

class GetHealthyFastFoodMealsUI(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase,
    private val outputController: (String) -> Unit
) {





    fun showHealthyFastFoodMeals() {
        getHealthyFastFoodUseCase.getHealthyFastFood().fold(
            onSuccess = { allMeals ->
                showMeals(allMeals)
            },
            onFailure = { error ->
                outputController("❌ Failed to load meals: ${error.message}")
            }
        )
    }

    private fun showMeals(allMeals: List<Meal>) {
        allMeals.forEachIndexed { index, meal ->
            outputController(
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
    }
}

