package Fake

import model.Meal
import model.Nutrition

fun createMeal(
    id: Int,
    name: String,
    ingredients: List<String>?,
) = Meal(
    id = id,
    name = name,
    ingredients = ingredients,
    minutes = null,
    description = "",
    contributorId = 122,
    submitted = null,
    numberOfSteps = null,
    numberOfIngredients = null,
    nutrition = Nutrition(null, null, null, null, null, null, null),
    tags = emptyList(),
    steps = emptyList(),
)