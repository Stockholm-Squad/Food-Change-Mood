package logic.usecases

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import logic.usecases.utils.MealCreationHandler
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetMealsForGymHelperUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMealsForGymHelperUseCaseTest {
    private lateinit var repository: MealsRepository
    private lateinit var getMealsForGymHelperUseCase: GetMealsForGymHelperUseCase
    private lateinit var mealCreationHandler: MealCreationHandler

    @Test
    fun `getGymHelperMeals() should return result list of meals when enter a valid proteins and calories with 20 percent difference`() {
        //Given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                mealCreationHandler.getGymHelperMeal("meal1", 20F, 50F),
                mealCreationHandler.getGymHelperMeal("meal2", 9F, 45F),
                mealCreationHandler.getGymHelperMeal("meal3", 10F, 45F),
            )
        )
        val desiredCalories = 10F
        val desiredProteins = 40F
        val approximateAmount = 20F

        //When
        val gymHelperMeals = getMealsForGymHelperUseCase.getGymHelperMeals(
            desiredCalories = desiredCalories,
            desiredProteins = desiredProteins,
            approximateAmount = approximateAmount
        )

        //Then
        Truth.assertThat(gymHelperMeals.getOrThrow()).isEqualTo(
            listOf(
                mealCreationHandler.getGymHelperMeal("meal2", 9F, 45F),
                mealCreationHandler.getGymHelperMeal("meal3", 10F, 45F),
            )
        )
    }

    @Test
    fun `getGymHelperMeals() should return result failure with message No meals match the desired protein and calories!! when enter a non valid proteins and calories with 20 percent difference`() {
        //Given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                mealCreationHandler.getGymHelperMeal("meal1", 20F, 50F),
                mealCreationHandler.getGymHelperMeal("meal2", 20F, 50F),
                mealCreationHandler.getGymHelperMeal("meal3", 20F, 50F),
            )
        )
        val desiredCalories = 10F
        val desiredProteins = 40F
        val approximateAmount = 20F

        //When
        val gymHelperMeals = getMealsForGymHelperUseCase.getGymHelperMeals(
            desiredCalories = desiredCalories,
            desiredProteins = desiredProteins,
            approximateAmount = approximateAmount
        )

        //Then
        assertThrows<Throwable> { gymHelperMeals.getOrThrow() }
    }

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        getMealsForGymHelperUseCase = GetMealsForGymHelperUseCase(repository)
        mealCreationHandler = MealCreationHandler()
    }
}