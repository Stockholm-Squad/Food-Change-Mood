package org.example.presentation.features

import org.example.logic.usecases.GetHealthyFastFoodUseCase

class GetHealthyFastFoodMealsUI(
    private val getHealthFastFoodUseCase: GetHealthyFastFoodUseCase
) {

    fun showHealthyFastFoodMeals() {
        getHealthFastFoodUseCase.getHealthyFastFood().forEachIndexed { index, meal ->
            println(
                "Meal ${index + 1}: " +
                        "Name='${meal.name}'\n " +
                        "ID=${meal.id}\n " +
                        "Minutes=${meal.minutes}\n " +
                        "ContributorID=${meal.contributorId}\n " +
                        "Submitted='${meal.submitted}\n, " +
                        "Tags=${meal.tags}\n " +
                        "Nutrition=${meal.nutrition}\n " +
                        "StepsCount=${meal.numberOfSteps}\n " +
                        "Steps=${meal.steps}\n " +
                        "Description='${meal.description?.take(30)}...'\n " + // to avoid long prints
                        "Ingredients=${meal.ingredients}\n " +
                        "IngredientsCount=${meal.numberOfIngredients}\n\n"
            )
        }
    }
}