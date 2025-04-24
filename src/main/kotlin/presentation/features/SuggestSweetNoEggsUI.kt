package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetDessertsWithNoEggsUseCase
import org.example.utils.Constants

class SuggestSweetNoEggsUI(
    private val getSweetWithNoEggs: GetDessertsWithNoEggsUseCase,
    private val reader: InputReader<String>,
    private val printer: OutputPrinter
) {

    fun handleSweetsNoEggs() {
        printer.printLine("🍬 Craving dessert? Here’s something sweet with zero eggs!")

        getSweetWithNoEggs.getDessertsWithNNoEggs()
            .onSuccess { allMeals ->
                showResultsRandomly(allMeals.toMutableList())
            }
            .onFailure { printer.printLine(Constants.NO_MEALS_FOUND_MATCHING) }
    }

    private fun showResultsRandomly(model: MutableList<Meal>) {
        do {
            val index = (0..model.size).random()
            printer.printLine("Dessert: ${model[index].name}\nDescription: ${model[index].description}\n")
            printer.printLine("Do you like this dessert?.   (y/n)")
            if (reader.read() == "y") {
                printer.printLine("\nMeal Name: ${model[index].name}\nMeal Description: ${model[index].description}\nMeal Ingredients: ${model[index].ingredients}\nMeal preparation steps: ${model[index].steps}\n")
                return
            }
            model.removeAt(index)
        } while (model.isNotEmpty())
    }


}