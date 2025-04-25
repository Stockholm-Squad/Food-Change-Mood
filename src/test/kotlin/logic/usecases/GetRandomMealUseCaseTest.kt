package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.output.OutputPrinter
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetRandomMealUseCase
import org.example.model.FoodChangeMoodExceptions
import org.example.utils.Constants
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import utils.buildMeal
import kotlin.test.Test

class GetRandomMealUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var getRandomMealUseCase: GetRandomMealUseCase


    @BeforeEach
    fun setUp(){
       mealsRepository= mockk(relaxed = true)
       getRandomMealUseCase= GetRandomMealUseCase(mealsRepository)
    }
    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }
    @Test
    fun `getRandomMeal() should return a random meal when repository returns success`(){
        //Given
     val meals = listOf(
         buildMeal(2,"aww  marinated olives",15),
         buildMeal(2,"aww  marinated olives",15),
         buildMeal(2,"aww  marinated olives",15),
         buildMeal(2,"aww  marinated olives",15),
     )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
       val result= getRandomMealUseCase.getRandomMeal()

        //Then
        assertThat(result.getOrNull()).isEqualTo( buildMeal(2,"aww  marinated olives",15))
    }

    @Test
    fun `getRandomMeal() should return failure when mealRepository return failure`(){
        // Given
        every { mealsRepository.getAllMeals() } returns Result.failure(FoodChangeMoodExceptions.LogicException.NoMealsFound())

        //When
        val result=getRandomMealUseCase.getRandomMeal()

        // Then
        assertThrows<FoodChangeMoodExceptions.LogicException.NoMealsFound> {result.getOrThrow() }
    }
    @Test
    fun `getRandomMeal() should return failure when meals list is empty`() {
        // Given
        every { mealsRepository.getAllMeals() } returns Result.success(emptyList())

        // Then & Then
        assertThrows<FoodChangeMoodExceptions.LogicException.NoMealsFound> {getRandomMealUseCase.getRandomMeal() }

    }

}

