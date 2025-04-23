package utils

import model.Meal
import model.Nutrition
import java.util.Date

fun buildMeal(
    id: Int,
    name: String? = "None",
    minutes: Int? = null,
    contributorId: Int = 0,
    submitted: Date? = null,
    tags: List<String>? = null,
    numberOfSteps: Int? = null,
    steps: List<String>? = null,
    description: String? = null,
    ingredients: List<String>? = null,
    numberOfIngredients: Int? = null,
    calories: Float? = null,
    totalFat: Float? = null,
    sugar: Float? = null,
    sodium: Float? = null,
    protein: Float? = null,
    saturatedFat: Float? = null,
    carbohydrates: Float? = null
): Meal {

    return Meal(
        name = name,
        id = id,
        minutes = minutes,
        contributorId = contributorId,
        submitted = submitted,
        tags = tags,
        nutrition = Nutrition(
            calories = calories,
            totalFat = totalFat,
            sugar = sugar,
            sodium = sodium,
            protein = protein,
            saturatedFat = saturatedFat,
            carbohydrates = carbohydrates
        ),
        numberOfSteps = numberOfSteps,
        steps = steps,
        description = description,
        ingredients = ingredients,
        numberOfIngredients = numberOfIngredients
    )
}