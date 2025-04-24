package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetHealthyFastFoodUseCase
import org.example.utils.Constants
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import utils.buildNutrition
import kotlin.test.Test

class GetHealthyFastFoodUseCaseTest {


    private lateinit var mealsRepository: MealsRepository
    private lateinit var getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase


    @BeforeEach
    fun setUp() {
        mealsRepository = mockk(relaxed = true)
        getHealthyFastFoodUseCase = GetHealthyFastFoodUseCase(mealsRepository)
    }

    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }

    @Test
    fun `getHealthyFastFood () should return meals with preparation time less than or equal 15 sorted by nutrition`() {
        //Given
        val meals = listOf(
            buildMeal(
                1,
                "souper  easy sweet   sour meatballs",
                10,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
            buildMeal(
                1,
                "sour cream  avocado dip  vegan",
                10,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
            buildMeal(
                1,
                "easiest dr  pepper ham glaze ever",
                13,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
            buildMeal(
                1,
                "favorite chinese steamed whole fish by sy",
                45,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            )
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()
        //Then

        assertThat(result.getOrThrow()).isEqualTo(
            listOf(
                buildMeal(
                    1,
                    "souper  easy sweet   sour meatballs",
                    10,
                    nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
                ),
                buildMeal(
                    1,
                    "sour cream  avocado dip  vegan",
                    10,
                    nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
                ),
                buildMeal(
                    1,
                    "easiest dr  pepper ham glaze ever",
                    13,
                    nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
                ),
            )
        )


    }

    @Test
    fun `getHealthyFastFood () should sort meals by totalFat, then saturatedFat, then carbohydrates`() {
        // Given
        val meal1 = buildMeal(
            1, "Meal A", 10,
            nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 10f)
        )
        val meal2 = buildMeal(
            2, "Meal B", 10,
            nutrition = buildNutrition(totalFat = 5f, saturatedFat = 3f, carbohydrates = 8f)
        )
        val meal3 = buildMeal(
            3, "Meal C", 10,
            nutrition = buildNutrition(totalFat = 5f, saturatedFat = 3f, carbohydrates = 5f)
        )
        val meal4 = buildMeal(
            4, "Meal D", 10,
            nutrition = buildNutrition(totalFat = 5f, saturatedFat = 2f, carbohydrates = 20f)
        )
        val meal5 = buildMeal(
            5, "Meal E", 20,
            nutrition = buildNutrition(totalFat = 1f, saturatedFat = 1f, carbohydrates = 1f)
        )

        every { mealsRepository.getAllMeals() } returns Result.success(
            listOf(meal1, meal2, meal3, meal4, meal5)
        )

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            listOf(meal4, meal3, meal2, meal1)
        )
    }


    @Test
    fun `getHealthyFastFood () should return failure when mealRepository return failure`() {
        // Given
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable())

        //When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        val exception = result.exceptionOrNull()
        assertThat(exception).isNotNull()

        val errorMessage = exception?.message
        assertThat(errorMessage).isNotNull()

        assertThat(errorMessage).contains(Constants.ERROR_FETCHING_MEALS)

    }

    @Test
    fun `getHealthyFastFood() should return empty list when all meals take longer than 15 minutes`() {
        // Given
        val meals = listOf(
            buildMeal(
                1,
                "souper  easy sweet   sour meatballs",
                100,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
            buildMeal(
                1,
                "sour cream  avocado dip  vegan",
                145,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
            buildMeal(
                1,
                "easiest dr  pepper ham glaze ever",
                130,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEmpty()
    }

    @Test
    fun `getHealthyFastFood() should return only meal when other meals take longer than 15 minutes`() {
        // Given
        val meals = listOf(
            buildMeal(
                1,
                "souper  easy sweet   sour meatballs",
                10,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
            buildMeal(
                1,
                "sour cream  avocado dip  vegan",
                145,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
            buildMeal(
                1,
                "easiest dr  pepper ham glaze ever",
                130,
                nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
            ),
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            listOf(
                buildMeal(
                    1,
                    "souper  easy sweet   sour meatballs",
                    10,
                    nutrition = buildNutrition(totalFat = 114.6f, saturatedFat = 6.0f, carbohydrates = 4.0f)
                )
            )
        )
    }
    @Test
    fun `getHealthyFastFood() should exclude meals with null nutrition`() {
        // Given
        val meals = listOf(
            buildMeal(1, "Meal with nutrition", 10, nutrition = buildNutrition(10f, 5f, 2f)),
            buildMeal(2, "Meal without nutrition", 10, nutrition = null)
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            listOf(
                buildMeal(1, "Meal with nutrition", 10, nutrition = buildNutrition(10f, 5f, 2f))
            )
        )
    }
    @Test
    fun `getHealthyFastFood() should exclude meals with null preparation time`() {
        // Given
        val meals = listOf(
            buildMeal(1, "Valid meal", 10, nutrition = buildNutrition(10f, 5f, 2f)),
            buildMeal(2, "Null time meal", null, nutrition = buildNutrition(1f, 1f, 1f))
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            listOf(
                buildMeal(1, "Valid meal", 10, nutrition = buildNutrition(10f, 5f, 2f))
            )
        )
    }
    @Test
    fun `getHealthyFastFood() should return empty when all meals have null nutrition`() {
        // Given
        val meals = listOf(
            buildMeal(1, "No nutrition 1", 10, nutrition = null),
            buildMeal(2, "No nutrition 2", 13, nutrition = null)
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEmpty()
    }
    @Test
    fun `getHealthyFastFood() should maintain order for meals with equal nutrition`() {
        // Given
        val meal1 = buildMeal(1, "Meal A", 13, nutrition = buildNutrition(5f, 2f, 3f))
        val meal2 = buildMeal(2, "Meal B", 10, nutrition = buildNutrition(5f, 2f, 3f))
        val meal3 = buildMeal(3, "Meal C", 14, nutrition = buildNutrition(5f, 2f, 3f))

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1, meal2, meal3))

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(listOf(meal1, meal2, meal3))
    }



}