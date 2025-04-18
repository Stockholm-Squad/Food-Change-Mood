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
) {
    override fun toString(): String {
        return """Meal(
        name= ${name ?: "N/A"},
        id= $id,
        minutes= ${minutes ?: "N/A"},
        contributorId= $contributorId,
        submitted= ${submitted?.toString() ?: "N/A"},
        tags= ${tags?.joinToString(", ") ?: "N/A"},
        nutrition= $nutrition,
        numberOfSteps= ${numberOfSteps ?: "N/A"},
        steps= ${steps?.joinToString("; ") ?: "N/A"},
        description= ${description ?: "N/A"},
        ingredients= ${ingredients?.joinToString(", ") ?: "N/A"},
        numberOfIngredients= ${numberOfIngredients ?: "N/A"}
    )
        """.trimIndent()
    }
}