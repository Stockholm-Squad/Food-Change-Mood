package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.createListOfMeals
import model.exceptedMeals
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetHealthyFastFoodUseCase
import org.example.model.FoodChangeMoodExceptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
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
    fun `getHealthyFastFood() should return meals filtered and sorted by nutrition`() {
        // Given
        val meals = createListOfMeals() +
                buildMeal(
                    id = 3,
                    name = "Unhealthy Meal",
                    minutes = 20,
                    nutrition = buildNutrition(totalFat = 15f, saturatedFat = 6f, carbohydrates = 20f),
                    tags = listOf("fast")
                )

        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            exceptedMeals()
        )
    }

    @Test
    fun `getHealthyFastFood() should return meals filtered by tags`() {
        // Given
        val meals = createListOfMeals()+
            buildMeal(
                id = 3,
                name = "Unhealthy Meal",
                minutes = 10,
                nutrition = buildNutrition(totalFat = 15f, saturatedFat = 6f, carbohydrates = 20f),
                tags = listOf("fast")
            )

        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            exceptedMeals()
        )
    }

    @Test
    fun `getHealthyFastFood() should return meals filtered by tags null`() {
        // Given
        val meals = createListOfMeals()+
            buildMeal(
                id = 3,
                name = "Unhealthy Meal",
                minutes = 10,
                nutrition = buildNutrition(totalFat = 15f, saturatedFat = 6f, carbohydrates = 20f),
                tags = null
            )

        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            exceptedMeals()
        )
    }

    @Test
    fun `getHealthyFastFood() should return failure when mealsRepository returns failure`() {
        // Given
        every { mealsRepository.getAllMeals() } returns Result.failure(FoodChangeMoodExceptions.LogicException.NoMealsFound())
        //When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()
        // Then
        assertThrows<FoodChangeMoodExceptions.LogicException.NoMealsFound> { result.getOrThrow() }
    }

    @Test
    fun `getHealthyFastFood() should return empty list when no meals meet the filter criteria`() {
        // Given
        val meals = listOf(
            buildMeal(
                id = 1,
                name = "Unhealthy Meal 1",
                minutes = 20,
                nutrition = buildNutrition(totalFat = 15f, saturatedFat = 6f, carbohydrates = 20f),
                tags = listOf("fast")
            ),
            buildMeal(
                id = 2,
                name = "Unhealthy Meal 2",
                minutes = 30,
                nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f),
                tags = listOf("fast")
            )
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)


        // When & Then
        assertThrows<FoodChangeMoodExceptions.LogicException.NoMealsFound> {
            getHealthyFastFoodUseCase.getHealthyFastFood()
        }
    }

    @Test
    fun `getHealthyFastFood() should return only one meal when other meals fail the filter`() {
        // Given
        val meals = listOf(
            buildMeal(
                id = 1,
                name = "Healthy Meal",
                minutes = 10,
                nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f),
                tags = listOf("healthy")
            ),
            buildMeal(
                id = 2,
                name = "Unhealthy Meal",
                minutes = 20,
                nutrition = buildNutrition(totalFat = 15f, saturatedFat = 6f, carbohydrates = 20f),
                tags = listOf("fast")
            )
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            listOf(
                buildMeal(
                    id = 1,
                    name = "Healthy Meal",
                    minutes = 10,
                    nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f),
                    tags = listOf("healthy")
                )
            )
        )
    }

    @Test
    fun `getHealthyFastFood() should exclude meals with null nutrition`() {
        // Given
        val meals = createListOfMeals()+
            buildMeal(id = 2, name = "Null Nutrition Meal", minutes = 10, nutrition = null, tags = listOf("healthy"))

        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            exceptedMeals()
        )
    }

    @Test
    fun `getHealthyFastFood() should exclude meals with null preparation time`() {
        // Given
        val meals = createListOfMeals()+
            buildMeal(
                id = 2,
                name = "Null Time Meal",
                minutes = null,
                nutrition = buildNutrition(totalFat = 5f, saturatedFat = 3f, carbohydrates = 10f),
                tags = listOf("healthy")
            )

        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            exceptedMeals()
        )
    }

    @Test
    fun `getHealthyFastFood() should return empty list when all meals have null nutrition`() {
        // Given
        val meals = listOf(
            buildMeal(id = 1, name = "No Nutrition 1", minutes = 10, nutrition = null, tags = listOf("healthy")),
            buildMeal(id = 2, name = "No Nutrition 2", minutes = 10, nutrition = null, tags = listOf("healthy"))
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When & Then
        assertThrows<FoodChangeMoodExceptions.LogicException.NoMealsFound> {
            getHealthyFastFoodUseCase.getHealthyFastFood()
        }
    }

    @Test
    fun `getHealthyFastFood() should maintain order for meals with equal nutrition`() {
        // Given
        val meal1 = buildMeal(
            id = 1,
            name = "Meal A",
            minutes = 10,
            nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f),
            tags = listOf("healthy")
        )
        val meal2 = buildMeal(
            id = 2,
            name = "Meal B",
            minutes = 10,
            nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f),
            tags = listOf("healthy")
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1, meal2))

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(listOf(meal1, meal2))
    }

    @Test
    fun `getHealthyFastFood() should sort meals by totalFat, then saturatedFat, then carbohydrates`() {
        // Given
        val meals = listOf(
            buildMeal(
                id = 1,
                name = "Meal A",
                minutes = 10,
                nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f),
                tags = listOf("healthy")
            ),
            buildMeal(
                id = 2,
                name = "Meal B",
                minutes = 10,
                nutrition = buildNutrition(totalFat = 8f, saturatedFat = 3f, carbohydrates = 12f),
                tags = listOf("healthy")
            ),
            buildMeal(
                id = 3,
                name = "Meal C",
                minutes = 10,
                nutrition = buildNutrition(totalFat = 5f, saturatedFat = 2f, carbohydrates = 10f),
                tags = listOf("healthy")
            )
        )

        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        // When
        val result = getHealthyFastFoodUseCase.getHealthyFastFood()

        // Then
        assertThat(result.getOrThrow()).isEqualTo(
            listOf(
                buildMeal(
                    id = 3,
                    name = "Meal C",
                    minutes = 10,
                    nutrition = buildNutrition(totalFat = 5f, saturatedFat = 2f, carbohydrates = 10f),
                    tags = listOf("healthy")
                ),
                buildMeal(
                    id = 2,
                    name = "Meal B",
                    minutes = 10,
                    nutrition = buildNutrition(totalFat = 8f, saturatedFat = 3f, carbohydrates = 12f),
                    tags = listOf("healthy")
                ),
                buildMeal(
                    id = 1,
                    name = "Meal A",
                    minutes = 10,
                    nutrition = buildNutrition(totalFat = 10f, saturatedFat = 5f, carbohydrates = 15f),
                    tags = listOf("healthy")
                )
            )
        )
    }
}