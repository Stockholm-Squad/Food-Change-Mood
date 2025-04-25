package model

import utils.buildMeal
import utils.buildNutrition

 fun createListOfMeals(): List<Meal> {
    val  meals=listOf(
        buildMeal(id = 1, name = "Healthy Meal 1", minutes = 10, nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f), tags = listOf("healthy")),
        buildMeal(id = 2, name = "Healthy Meal 2", minutes = 10, nutrition = buildNutrition(totalFat = 8f, saturatedFat = 4f, carbohydrates = 12f), tags = listOf("healthy")),
    )
return meals.toList()
}
fun exceptedMeals(): List<Meal>{
    val  meals=listOf(
        buildMeal(id = 2, name = "Healthy Meal 2", minutes = 10, nutrition = buildNutrition(totalFat = 8f, saturatedFat = 4f, carbohydrates = 12f), tags = listOf("healthy")),
        buildMeal(id = 1, name = "Healthy Meal 1", minutes = 10, nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f), tags = listOf("healthy")),
    )
    return meals.toList()
}
