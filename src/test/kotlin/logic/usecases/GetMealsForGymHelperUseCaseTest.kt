package logic.usecases

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecases.utils.MealCreationHandler
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetMealsForGymHelperUseCase
import org.example.model.FoodChangeMoodExceptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GetMealsForGymHelperUseCaseTest {
    private lateinit var repository: MealsRepository
    private lateinit var getMealsForGymHelperUseCase: GetMealsForGymHelperUseCase
    private lateinit var mealCreationHandler: MealCreationHandler

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        getMealsForGymHelperUseCase = GetMealsForGymHelperUseCase(repository)
        mealCreationHandler = MealCreationHandler()
    }

    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { repository.getAllMeals() }
    }

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
    fun `getGymHelperMeals() should return result failure with NoMealsForGymHelperException when enter proteins and calories not within 20 percent difference`() {
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
        assertThrows<FoodChangeMoodExceptions.LogicException.NoMealsForGymHelperException> { gymHelperMeals.getOrThrow() }
    }

    @Test
    fun `getGymHelperMeals() should return result failure with Throwable when getAllMeals() returns result with failure`() {
        //Given
        every { repository.getAllMeals() } returns Result.failure(Throwable())

        val desiredCalories = 10F
        val desiredProteins = 40F

        //When
        val gymHelperMeals = getMealsForGymHelperUseCase.getGymHelperMeals(
            desiredCalories = desiredCalories,
            desiredProteins = desiredProteins
        )

        //Then
        assertThrows<Throwable> { gymHelperMeals.getOrThrow() }
    }

    @Test
    fun `getGymHelperMeals() should return result failure with NoMealsForGymHelperException when meals have nullable nutrition`() {
        //Given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                mealCreationHandler.getGymHelperMealWithNullNutrition("meal1"),
                mealCreationHandler.getGymHelperMealWithNullNutrition("meal2"),
                mealCreationHandler.getGymHelperMealWithNullNutrition("meal3"),
            )
        )

        val desiredCalories = 10F
        val desiredProteins = 40F

        //When
        val gymHelperMeals = getMealsForGymHelperUseCase.getGymHelperMeals(
            desiredCalories = desiredCalories,
            desiredProteins = desiredProteins
        )

        //Then
        assertThrows<FoodChangeMoodExceptions.LogicException.NoMealsForGymHelperException> { gymHelperMeals.getOrThrow() }
    }

    @Test
    fun `getGymHelperMeals() should return result failure with NoMealsForGymHelperException when nutrition in meals have nullable calories or proteins`() {
        //Given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                mealCreationHandler.getGymHelperMeal("meal1", null, 40F),
                mealCreationHandler.getGymHelperMeal("meal2", 10F, null),
            )
        )

        val desiredCalories = 10F
        val desiredProteins = 40F

        //When
        val gymHelperMeals = getMealsForGymHelperUseCase.getGymHelperMeals(
            desiredCalories = desiredCalories,
            desiredProteins = desiredProteins
        )

        //Then
        assertThrows<FoodChangeMoodExceptions.LogicException.NoMealsForGymHelperException> { gymHelperMeals.getOrThrow() }
    }

    @ParameterizedTest
    @CsvSource(
        "10, 20",
        "20, 40",
        "40, 100"
    )
    fun `getGymHelperMeals() should return result with list of meals when valid calories and proteins`(
        calories: Float, proteins: Float
    ) {
        //Given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                mealCreationHandler.getGymHelperMeal("meal", 10F, 20F),
                mealCreationHandler.getGymHelperMeal("meal", 20F, 40F),
                mealCreationHandler.getGymHelperMeal("meal", 40F, 100F),
            )
        )
        val approximateAmount = 20F

        //When
        val gymHelperMeals = getMealsForGymHelperUseCase.getGymHelperMeals(
            desiredCalories = calories,
            desiredProteins = proteins,
            approximateAmount = approximateAmount
        )

        //Then
        Truth.assertThat(gymHelperMeals.getOrThrow()).isEqualTo(
            listOf(
                mealCreationHandler.getGymHelperMeal("meal", calories, proteins),
            )
        )
    }
}