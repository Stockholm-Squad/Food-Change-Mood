package logic.usecases

import model.Meal
import model.Nutrition

fun createMeal(
    mealName: String,
    minutes:Int,
    mealNutrition: Nutrition
)= Meal(
    name = mealName,
    id = 0,
    minutes = minutes,
    contributorId = 0,
    submitted = null,
    tags = null,
    nutrition = mealNutrition,
    numberOfSteps = null,
    steps =null,
    description = null,
    ingredients = null,
    numberOfIngredients = null
)
fun createNutrition(
    totalFat:Float?,
    saturatedFat: Float?,
    carbohydrates: Float?
)= Nutrition(
    calories =0.0f,
    totalFat = totalFat,
    sugar = 0.0f,
    sodium =0.0f,
    protein = 0.0f,
    saturatedFat = saturatedFat,
    carbohydrates = carbohydrates
)
