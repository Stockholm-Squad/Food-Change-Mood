package org.example.logic.usecases.model

data class IngredientQuestionModel (
    val mealName: String,
    val options: List<String>,
    val correctIngredient: String
)


