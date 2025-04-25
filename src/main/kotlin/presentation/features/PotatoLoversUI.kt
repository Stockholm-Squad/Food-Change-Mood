package org.example.presentation.features
import model.Meal
import org.example.logic.usecases.GetPotatoMealsUseCase
import org.example.utils.Constants
import org.example.utils.InputReader
import org.example.utils.OutputPrinter


class PotatoLoversUI(
    private val getPotatoMealsUseCase: GetPotatoMealsUseCase,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader
) {

    fun showPotatoLoversUI(count: Int = 10) {
        outputPrinter.printLine(Constants.I_LOVE_POTATO_HERE+"$count "+ Constants.MEAL_INCLUDE_POTATO+"\n")
        getPotatoMealsUseCase.getRandomPotatoMeals(count)
        askToViewMealDetails()
        askIfWantsMore()
    }


    private fun askToViewMealDetails(meals: List<Meal?>) {
        var validInput = false
        do {
            outputPrinter.printLine("\n"+ Constants.VIEW_MEAL_DETAILS)

            val input = inputReader.readLineOrNull()!!.trim().lowercase()


            if ( input == "n") {
                outputPrinter.printLine(Constants.ENJOY_YOUR_MEAL)
                return
            }

            val selectedIndex = input.toIntOrNull()
            if (selectedIndex != null && selectedIndex in 1..meals.size) {
                meals[selectedIndex - 1]?.let { showMealDetails(it) }
                validInput = true
            } else {
                outputPrinter.printLine(Constants.INVALID_SELECTION)
            }
        } while (!validInput)
    }

    private fun showMealDetails(meal: Meal) {
        outputPrinter.printLine("\n" + Constants.DETAILS_MEAL+ "'${meal.name}':")
        outputPrinter.printLine(Constants.MINUTE_TO_PREPARE + "${meal.minutes}")
        outputPrinter.printLine(Constants.NUMBER_OF_STEP + "${meal.numberOfSteps}")

        outputPrinter.printLine(Constants.STEPS)
        meal.steps?.forEachIndexed { index, step ->
            outputPrinter.printLine("${index + 1}. $step")
        }

        outputPrinter.printLine(Constants.DESCRIPTION +"${meal.description}")
        outputPrinter.printLine(Constants.NUTRITION+"${meal.nutrition}")
        outputPrinter.printLine(Constants.NUMBER_OF_INGREDIENT + " ${meal.numberOfIngredients}")

        outputPrinter.printLine(Constants.INGREDIENT)
        meal.ingredients?.forEachIndexed { index, ingredient ->
            outputPrinter.printLine("   ${index + 1}. $ingredient")
        } ?: outputPrinter.printLine(Constants.NA)
    }

    private fun askIfWantsMore(onYes: () -> Unit = { showPotatoLoversUI() }) {
        outputPrinter.printLine(Constants.SEE_MORE_MEALS)
        val input = inputReader.readLineOrNull()

        val normalizedAnswer = normalizeInput(input)

        if (normalizedAnswer == Constants.YES) {
            onYes()
        } else {
            outputPrinter.printLine(Constants.ENJOY_YOUR_MEAL)
        }
    }


    private fun normalizeInput(input: String?): String {
        if (input == null) return ""
        return input.trim().lowercase()
    }


}