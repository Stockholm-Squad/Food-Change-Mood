package model

import utils.buildMeal
import utils.buildNutrition

 fun createListOfMeals(): List<Meal> {
    val  meals=listOf(
         buildMeal(
            1, "Meal A", 10,
    nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 10f)
    ),
    buildMeal(
        2, "Meal B", 10,
        nutrition = buildNutrition(totalFat = 5f, saturatedFat = 3f, carbohydrates = 8f)
    ),
    buildMeal(
        3, "Meal C", 10,
        nutrition = buildNutrition(totalFat = 5f, saturatedFat = 3f, carbohydrates = 5f)
    ),
    buildMeal(
        4, "Meal D", 10,
        nutrition = buildNutrition(totalFat = 5f, saturatedFat = 2f, carbohydrates = 20f)
    ),
     buildMeal(
        5, "Meal E", 20,
        nutrition = buildNutrition(totalFat = 1f, saturatedFat = 1f, carbohydrates = 1f)
    )
    )
return meals.toList()
}
