package org.example.presentation.features

import model.Meal
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetCountryMealsUseCase
import org.example.utils.Constants

class ExploreCountryFoodUI(
    private val getCountriesFood: GetCountryMealsUseCase,
    private val stringReader: InputReader<String>,
    private val printer: OutputPrinter
) {

    fun exploreCountryFoodCulture() {
        printer.printLine("🌍 Let's take your taste buds on a world tour!")
        printer.printLine("Enter the country you want to explore: ")

        val countryName = getCountryName() ?: return showInvalidInput()

        displayMealsFromCountry(countryName)
    }

    private fun getCountryName(): String? {
        return stringReader.read()?.trim()?.takeIf { it.isNotBlank() }
    }

    private fun displayMealsFromCountry(countryName: String) {
        printer.printLine("\n---------------------------------- Here are 20 random meals from $countryName ----------------------------------\n")

        getCountriesFood.getMealsForCountry(countryName).fold(
            onSuccess = { meals -> handleSuccess(meals) },
            onFailure = { handleFailure() }
        )
    }

    private fun handleSuccess(meals: List<Meal>) {
        meals.forEach { meal ->
            printer.printLine(formatMealDetails(meal))
        }
    }

    private fun handleFailure() {
        printer.printLine("❌ No meals found for the selected country.")
    }

    private fun showInvalidInput() {
        printer.printLine(Constants.INVALID_INPUT)
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
