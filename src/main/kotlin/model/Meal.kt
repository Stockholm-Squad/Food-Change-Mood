package model

import java.util.*

data class Meal(
    val id: Int,
    val name: String?,
    val description: String?,
    val tags: List<String>?,
    val numberOfIngredients:Int?,
    val ingredients: List<String>?,
    val numberOfSteps:Int?,
    val steps: List<String>?,
    val preparationTime: Int?,
    val nutrition: Nutrition,
    val addedDate: Date
)
