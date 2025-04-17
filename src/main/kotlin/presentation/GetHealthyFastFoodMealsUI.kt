package presentation;

import logic.GetHealthFastFoodUseCase

class GetHealthyFastFoodMealsUI(
    private val getHealthFastFoodUseCase: GetHealthFastFoodUseCase
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