package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetItalianMealsForLargeGroupUseCase
import org.example.utils.Constants.NO_ITALIAN_MEALS_FOR_LARGE_GROUP_FOUND

class ItalianLargeGroupMealsUI(
    private val getItalianMealsForLargeGroupUseCase: GetItalianMealsForLargeGroupUseCase,
    private val reader: InputReader,
    private val printer: OutputPrinter
) {

    fun italianLargeGroupMealsUI() {
        printer.printLine("🍝 Planning a big Italian feast? Here's a list of meals perfect for large groups:")
        printer.printLine("Loading...")

        getItalianMealsForLargeGroupUseCase.getItalianMealsForLargeGroup().fold(
            onSuccess = { meals -> meals },
            onFailure = { exception ->
                printer.printLine("Error: ${exception.message ?: "Unknown Error"}")
                emptyList()
            }
        ).also {
            displayMealsAndHandleInteraction(it)
        }
    }

    private fun printMealsIdName(mealsList: List<Meal>) {
        if (mealsList.isEmpty()) {
            printer.printLine(NO_ITALIAN_MEALS_FOR_LARGE_GROUP_FOUND)
            return
        }
        mealsList.forEach { meal ->
            printer.printLine("${meal.id} -> ${meal.name}")
        }
    }

    private fun displayMealsAndHandleInteraction(meals: List<Meal>) {
        printMealsIdName(meals)

        while (true) {
            printer.printLine("")
            printer.printLine("-1 -> back")
            printer.printLine("meal id -> view details")
            val input = reader.readStringOrNull()

            if (viewMealDetails(input, meals)) break
        }
    }

    private fun viewMealDetails(input: String?, meals: List<Meal>): Boolean {
        when (val mealId = input?.toIntOrNull()) {
            null -> printer.printLine("Enter a valid ID or -1")
            -1 -> return true
            else -> viewMealInListDetails(mealId, meals, printer)
        }
        return false
    }

    fun viewMealInListDetails(mealId: Int, list: List<Meal>, printer: OutputPrinter) {
        val meal: Meal? = list.find { meal ->
            meal.id == mealId
        }

        if (meal == null) {
            printer.printLine("The meal with ID $mealId does not exist.")
            return
        }

        printer.printMeal(meal)
    }
}