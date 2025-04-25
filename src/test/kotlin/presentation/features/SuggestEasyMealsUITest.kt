package org.example.presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetEasyFoodSuggestionsUseCase
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal

class SuggestEasyMealsUITest {

    private lateinit var suggestEasyMealsUI: SuggestEasyMealsUI
    private lateinit var getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp() {
        getEasyFoodSuggestionsUseCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        suggestEasyMealsUI = SuggestEasyMealsUI(getEasyFoodSuggestionsUseCase, printer)
    }

    @Test
    fun `showEasySuggestions should print header and meals when successful`() {
        // Given
        val meals = listOf(
            buildMeal(1, minutes = 20, numberOfIngredients = 4, numberOfSteps = 5)
            ,buildMeal(2, minutes = 30, numberOfIngredients = 5, numberOfSteps = 6)

        )
        every { getEasyFoodSuggestionsUseCase.getEasyFood() } returns Result.success(meals)

        // When
        suggestEasyMealsUI.showEasySuggestions()

        // Then
        verify {
            printer.printLine("⏱️ Easy meals coming up!")
            printer.printMeals(meals)
        }
    }

    @Test
    fun `showEasySuggestions should print header and message when no meals found`() {
        // Given
        every { getEasyFoodSuggestionsUseCase.getEasyFood() } returns
                Result.success(emptyList())

        // When
        suggestEasyMealsUI.showEasySuggestions()

        // Then
        verify {
            printer.printLine("⏱️ Easy meals coming up!")
            printer.printLine(Constants.NO_EASY_MEALS_FOUND)
        }
    }

    @Test
    fun `showEasySuggestions should print error message when failure occurs`() {
        // Given
        val errorMessage = "Database error"
        every { getEasyFoodSuggestionsUseCase.getEasyFood() } returns
                Result.failure(RuntimeException(errorMessage))

        // When
        suggestEasyMealsUI.showEasySuggestions()

        // Then
        verify {
            printer.printLine("⏱️ Easy meals coming up!")
            printer.printLine(errorMessage)
        }
    }

    @Test
    fun `showEasySuggestions should call use case exactly once`() {
        // Given
        every { getEasyFoodSuggestionsUseCase.getEasyFood() } returns
                Result.success(emptyList())

        // When
        suggestEasyMealsUI.showEasySuggestions()

        // Then
        verify(exactly = 1) { getEasyFoodSuggestionsUseCase.getEasyFood() }
    }
}