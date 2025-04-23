package logic.usecases

import model.Meal

fun createMeal (
    mealName: String,
    mealPreparationTime:Int
)= Meal(
    id=0,
    name = mealName,
    minutes=mealPreparationTime,
    tags = null,
    steps = null,
    nutrition = null,
    submitted = null,
    description = null,
    ingredients = null,
    contributorId = 0,
    numberOfSteps = null,
    numberOfIngredients = null,
)