package logic.usecases.getseafoodmealsbyproteintest

import utils.buildMeal
import utils.buildNutrition

private val allMeals = listOf(
    buildMeal(
        id = 1,
        name = "Pan fried fish",
        description = "Delicious seafood dish packed with protein",
        nutrition = buildNutrition(protein = 30f)
    ),
    buildMeal(
        id = 2,
        name = "Shrimp pasta",
        description = "A tasty seafood pasta",
        nutrition = buildNutrition(protein = 20f)
    ),
    buildMeal(
        id = 3,
        name = "Veggie salad",
        description = "Fresh salad with no seafood",
        nutrition = buildNutrition(protein = 5f)
    )
)

fun getSeaFoodMeals() = allMeals
