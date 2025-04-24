package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.output.OutputPrinter
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetRandomMealUseCase
import org.example.utils.Constants
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import kotlin.test.Test

class GetRandomMealUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var getRandomMealUseCase: GetRandomMealUseCase
    private lateinit var printer: OutputPrinter

    @BeforeEach
    fun setUp(){
       mealsRepository= mockk(relaxed = true)
        printer=mockk(relaxed = true)
       getRandomMealUseCase= GetRandomMealUseCase(mealsRepository)
    }
    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }
    @Test
    fun `getRandomMeal () should return a random meal when repository returns success`(){
        //Given
     val meals = listOf(
         buildMeal(1,"apple a day  milk shake",0),
         buildMeal(2,"aww  marinated olives",15),
         buildMeal(3,"backyard style  barbecued ribs",120),
         buildMeal(4,"bananas 4 ice cream  pie",180)
     )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
       val result= getRandomMealUseCase.getRandomMeal()

        //Then
        assertThat(meals).contains(result.getOrNull())
    }

    @Test
    fun `getRandomMeal () should return failure when mealRepository return failure`(){
        // Given
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable())

        //When
        val result=getRandomMealUseCase.getRandomMeal()

        // Then
        val exception = result.exceptionOrNull()
        assertThat(exception).isNotNull()

        val errorMessage = exception?.message
        assertThat(errorMessage).isNotNull()

        assertThat(errorMessage).contains(Constants.ERROR_FETCHING_MEALS)

    }
    @Test
    fun `should return failure when meals list is empty`() {
        // Given
        every { mealsRepository.getAllMeals() } returns Result.success(emptyList())

        // When
        val result = getRandomMealUseCase.getRandomMeal()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo(Constants.NO_MEALS_FOUND)
    }

}

