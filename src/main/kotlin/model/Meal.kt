package model

import kotlinx.datetime.LocalDate


data class Meal(
    val name: String?,
    val id: Int,
    val minutes: Int?,
    val contributorId: Int,
    val submitted: LocalDate?,
    val tags: List<String>?,
    val nutrition: Nutrition?,
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
        submitted= ${submitted ?: "N/A"},
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