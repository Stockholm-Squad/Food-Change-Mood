package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetHealthyFastFoodUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertTrue

class GetHealthyFastFoodUseCaseTest {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase


    @BeforeEach
    fun setUp() {
        mealsRepository = mockk(relaxed = true)
        getHealthyFastFoodUseCase = GetHealthyFastFoodUseCase(mealsRepository)
    }

    @Test
    fun `getHealthyFastFood should return meals with preparation time less than or equal 15 sorted by nutrition`() {
        //Given
        val meals = listOf(
            createMeal("souper  easy sweet   sour meatballs", 10, createNutrition(114.6f, 6.0f, 4.0f)),
            createMeal("sour cream  avocado dip  vegan", 10, createNutrition(1.0f, 0.0f, 0.0f)),
            createMeal("easiest dr  pepper ham glaze ever", 13, createNutrition(89.6f, 0.0f, 7.0f)),
            createMeal("favorite chinese steamed whole fish by sy", 45, createNutrition(0.0f, 0.0f, 0.0f))
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()
        //Then
        assertThat(result.isSuccess).isTrue()



    }
    @Test
    fun `getHealthyFastFood should return failure when repo failure`(){
        //Given
        val errorMessage="No meals Found"
        every { mealsRepository.getAllMeals()} returns Result.failure(Exception(errorMessage))
        //When
        val result=getHealthyFastFoodUseCase.getHealthyFastFood()
        //Then
        assertThat(result.isFailure).isTrue()
    }

}