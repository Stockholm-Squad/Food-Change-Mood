package model

import java.util.*

data class Meal(
    val name: String,
    val id: Int,
    val preparationTime: Int,
    val contributorId: Int,
    val addedDate: String,
    val tags: List<String>,
    val nutrition: Nutrition,
    val numberOfSteps: Int,
    val steps: List<String>,
    val description: String,
    val ingredients: List<String>,
    val numberOfIngredients: Int,
)
