package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetCountryMealsUseCase
import org.example.utils.Constants

class ExploreCountryMealsUI(
    private val getCountriesFood: GetCountryMealsUseCase,
    private val reader: InputReader<String>,
    private val printer: OutputPrinter
) {

    fun handleCountryByNameAction() {
        printer.printLine("🌍 Let's take your taste buds on a world tour!")
        printer.printLine("Enter the country you want to explore: ")

        reader.read()?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { countryName ->
                getCountriesFood.getMealsForCountry(countryName)
                    .onSuccess { meals -> displayMeals(meals, countryName) }
                    .onFailure { printer.printLine(Constants.INVALID_INPUT) }
            } ?: printer.printLine(Constants.INVALID_INPUT)
    }

    private fun displayMeals(meals: List<Meal>, countryName: String) {
        meals.takeIf { it.isNotEmpty() }?.let {
            printer.printLine(Constants.HERE_ARE_THE_MEALS)
            it.forEachIndexed { index, meal ->showMealDetails(meal) }

        } ?: printer.printLine(Constants.NO_MEALS_FOUND_MATCHING)
    }


    private fun showMealDetails(meal: Meal) {
        printer.printLine(formatMealDetails(meal))
    }


    private fun formatMealDetails(meal: Meal): String {
        return """
            |Meal Name: ${meal.name}
            |Meal Description: ${meal.description}
            |Meal Ingredients: ${meal.ingredients?.joinToString()}
            |Meal Preparation Steps: ${meal.steps?.joinToString()}
        """.trimMargin()
    }
}