package model

import java.util.Date


data class Meal(
    val name: String?,
    val id: Int, // Non-nullable because every meal must have an ID
    val minutes: Int?,
    val contributorId: Int, // Non-nullable because every meal has a contributor
    val submitted: Date?,
    val tags: List<String>?,
    val nutrition: Nutrition, // Non-nullable but with default values
    val numberOfSteps: Int?,
    val steps: List<String>?,
    val description: String?,
    val ingredients: List<String>?,
    val numberOfIngredients: Int?
)