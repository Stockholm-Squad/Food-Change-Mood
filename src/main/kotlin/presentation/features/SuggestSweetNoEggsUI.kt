package org.example.presentation.features

import model.Meal
import org.example.logic.usecases.GetDessertsWithNoEggsUseCase

class SuggestSweetNoEggsUI(private val getSweetWithNoEggs: GetDessertsWithNoEggsUseCase) {

    fun showSweetsNoEggs() {
        println("🍬 Craving dessert? Here’s something sweet with zero eggs!")


        getSweetWithNoEggs.getDessertsWithNNoEggs().onSuccess { allMeals ->
            showResultsRandomly(allMeals.toMutableList())
        }.onFailure { println("Couldn't Find Desserts you grave sorry") }
    }

    private fun showResultsRandomly(model: MutableList<Meal>) {
        while (model.isNotEmpty()) {
            val index = (0..model.size).random()
            println("Dessert: ${model[index].name}\nDescription: ${model[index].description}\n")
            println("Do you like this dessert?.   (y/n)")
            if (choosingAnotherMealDecision() == "y") {
                println("\nMeal Name: ${model[index].name}\nMeal Description: ${model[index].description}\nMeal Ingredients: ${model[index].ingredients}\nMeal preparation steps: ${model[index].steps}\n")
                return
            }
            model.removeAt(2)
        }
    }

    private fun choosingAnotherMealDecision(): String? {
        return readlnOrNull()
    }
}