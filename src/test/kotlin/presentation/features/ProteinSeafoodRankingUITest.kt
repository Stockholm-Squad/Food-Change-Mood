package presentation.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecases.getseafoodmealsbyproteintest.getSeaFoodMeals
import org.example.input_output.output.OutputPrinter
import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase
import org.example.model.FoodChangeMoodExceptions
import org.example.presentation.features.ProteinSeafoodRankingUI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProteinSeafoodRankingUITest() {
    private lateinit var getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase
    private lateinit var printer: OutputPrinter
    private lateinit var seafoodRankingUI: ProteinSeafoodRankingUI

    @BeforeEach
    fun setUp() {
        getSeaFoodByProteinRankUseCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        seafoodRankingUI = ProteinSeafoodRankingUI(getSeaFoodByProteinRankUseCase, printer)
    }
    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank() }
    }

    @Test
    fun `proteinSeafoodRanking() should print seafood ranked by protein when received meals`() {

        // Given
        val rankedMeals = getSeaFoodMeals()
        every { getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank() } returns Result.success(rankedMeals)

        //When
        seafoodRankingUI.proteinSeafoodRanking()

        //Then
        verify {
            rankedMeals.mapIndexed { index, meal ->
                printer.printLine("Rank: ${index + 1} Meal name: ${meal.name} Protein amount : ${meal.nutrition?.protein}")
            }
        }

    }

    @Test
    fun `proteinSeafoodRanking() should print exception message when received exception`() {

        // Given
        val exception = FoodChangeMoodExceptions.LogicException.NoSeaFoodMealsFound()
        every { getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank() } returns Result.failure(exception)

        // When
        seafoodRankingUI.proteinSeafoodRanking()

        // Then
        verify { printer.printLine(exception.message) }

    }

}