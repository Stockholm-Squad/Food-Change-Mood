package logic.usecases

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetMealsForGymHelperUseCase
import org.example.model.FoodChangeMoodExceptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import utils.buildMeal
import utils.buildNutrition

class GetMealsForGymHelperUseCaseTest {
    private lateinit var repository: MealsRepository
    private lateinit var getMealsForGymHelperUseCase: GetMealsForGymHelperUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        getMealsForGymHelperUseCase = GetMealsForGymHelperUseCase(repository)
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
                buildMeal(1, nutrition = buildNutrition(calories = 20F, protein = 50F)),
                buildMeal(2, nutrition = buildNutrition(calories = 9F, protein = 45F)),
                buildMeal(3, nutrition = buildNutrition(calories = 10F, protein = 45F)),
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
                buildMeal(2, nutrition = buildNutrition(calories = 9F, protein = 45F)),
                buildMeal(3, nutrition = buildNutrition(calories = 10F, protein = 45F)),
            )
        )
    }

    @Test
    fun `getGymHelperMeals() should return result failure with NoMealsForGymHelperException when enter proteins and calories not within 20 percent difference`() {
        //Given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                buildMeal(1, nutrition = buildNutrition(calories = 20F, protein = 50F)),
                buildMeal(2, nutrition = buildNutrition(calories = 20F, protein = 50F)),
                buildMeal(3, nutrition = buildNutrition(calories = 20F, protein = 50F)),
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
                buildMeal(1, nutrition = null),
                buildMeal(2, nutrition = null),
                buildMeal(3, nutrition = null),
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
                buildMeal(1, nutrition = buildNutrition(calories = null, protein = 40F)),
                buildMeal(2, nutrition = buildNutrition(calories = 10F, protein = null)),
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
                buildMeal(2, nutrition = buildNutrition(calories = 10F, protein = 20F)),
                buildMeal(2, nutrition = buildNutrition(calories = 20F, protein = 40F)),
                buildMeal(2, nutrition = buildNutrition(calories = 40F, protein = 100F)),
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
                buildMeal(2, nutrition = buildNutrition(calories = calories, protein = proteins)),
            )
        )
    }
}