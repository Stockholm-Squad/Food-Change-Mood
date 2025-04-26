package utils

import kotlinx.datetime.LocalDate
import model.Meal
import model.Nutrition

fun buildMeal(
    id: Int,
    name: String? = "None",
    minutes: Int? = null,
    contributorId: Int = 0,
    submitted: LocalDate? = null,
    tags: List<String>? = null,
    numberOfSteps: Int? = null,
    steps: List<String>? = null,
    description: String? = null,
    ingredients: List<String>? = null,
    numberOfIngredients: Int? = null,
    nutrition: Nutrition? = buildNutrition()
): Meal {

    return Meal(
        name = name,
        id = id,
        minutes = minutes,
        contributorId = contributorId,
        submitted = submitted,
        tags = tags,
        nutrition = nutrition,
        numberOfSteps = numberOfSteps,
        steps = steps,
        description = description,
        ingredients = ingredients,
        numberOfIngredients = numberOfIngredients
    )
}

fun buildNutrition(
    calories: Float? = null,
    totalFat: Float? = null,
    sugar: Float? = null,
    sodium: Float? = null,
    protein: Float? = null,
    saturatedFat: Float? = null,
    carbohydrates: Float? = null
): Nutrition {
    return Nutrition(
        calories = calories,
        totalFat = totalFat,
        sugar = sugar,
        sodium = sodium,
        protein = protein,
        saturatedFat = saturatedFat,
        carbohydrates = carbohydrates
    )
}