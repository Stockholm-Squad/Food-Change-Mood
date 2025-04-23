package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetRandomMealUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertTrue

class GetRandomMealUseCaseTest {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var getRandomMealUseCase: GetRandomMealUseCase

    @BeforeEach
    fun setUp(){
       mealsRepository= mockk(relaxed = true)
       getRandomMealUseCase= GetRandomMealUseCase(mealsRepository)
    }
    @Test
    fun `getRandomMeal should return a random meal when repository returns success`(){
        //Given
     val meals = listOf(
            createMeal("apple a day  milk shake",0),
            createMeal("aww  marinated olives",15),
            createMeal("backyard style  barbecued ribs",120),
            createMeal("bananas 4 ice cream  pie",180))
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
       val result= getRandomMealUseCase.getRandomMeal()

        //Then
        //assertThat(meals).contains(result.getOrNull())
        assertThat(meals).contains(result.getOrNull())
    }

    @Test
    fun `getRandomMeal should return failure when mealRepository return failure`(){
        // Given
        every { mealsRepository.getAllMeals() } returns Result.failure(Exception("Repo fail"))

        //When
        val result=getRandomMealUseCase.getRandomMeal()

        // Then
        assertTrue {result.isFailure }

    }


}

