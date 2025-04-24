package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetDessertsWithNoEggsUseCase
import org.example.utils.Constants

class SuggestSweetNoEggsUI(
    private val getSweetWithNoEggs: GetDessertsWithNoEggsUseCase,
    private val reader: InputReader,
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
            getRandomIndex(model.size).let { index ->
                printDessertBrief(model, index)

                if (continueDecision()) {
                    printDessertInDetails(model[index])
                    return
                }
                model.removeAt(index)
            }
        } while (model.isNotEmpty())
        printer.printLine(Constants.NO_MORE_DESSERTS_AVAILABLE)
    }

    private fun printDessertBrief(model: MutableList<Meal>, index: Int) {
        printer.printLine("Dessert: ${model[index].name}\nDescription: ${model[index].description}\n")
    }

    private fun getRandomIndex(size: Int): Int {
        return (0 until size).random()
    }

    private fun printDessertInDetails(model: Meal) {
        printer.printLine(
            "\nMeal Name: ${model.name}\n" +
                    "Meal Description: ${model.description}\nMeal Ingredients: ${model.ingredients}\n" +
                    "Meal preparation steps: ${model.steps}\n"
        )
    }

    private fun continueDecision(): Boolean {
        printer.printLine("Do you like this dessert?.   (y/n)")
        return reader.readLineOrNull() == "y"
    }


}