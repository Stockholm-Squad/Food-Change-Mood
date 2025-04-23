package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.usecases.utils.buildMeal
import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetMealsByDateUseCase
import org.example.utils.getDateFromString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetMealsByDateUseCaseTest {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var getMealsByDateUseCase: GetMealsByDateUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk(relaxed = true)
        getMealsByDateUseCase = GetMealsByDateUseCase(mealsRepository)
    }

    @Test
    fun `getMealsByDate(date) should return meals filtered by date`() {
        // Given
        val date = "2025-04-23"
        val utilDate = getDateFromString(date).getOrThrow()
        val meal1 = buildMeal(id = 1, submitted = utilDate)
        val meal2 = buildMeal(id = 2, submitted = Date()) // Different date
        val allMeals = listOf(meal1, meal2)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).containsExactly(meal1)
        }
    }

    @Test
    fun `getMealsByDate(date) should return empty list when no meals match the date`() {
        // Given
        val date = "2025-04-23"
        val meal1 = buildMeal(id = 1, submitted = Date()) // Different date
        val allMeals = listOf(meal1)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).isEmpty()
        }
    }

    @Test
    fun `getMealsByDate(date) should propagate failure when repository fails`() {
        // Given
        val date = "2025-04-23"
        val exception = Exception("Database error")

        every { mealsRepository.getAllMeals() } returns Result.failure(exception)

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        assertThat(result.isFailure).isTrue()
        result.onFailure { cause ->
            assertThat(cause).isEqualTo(exception)
        }
    }

    @Test
    fun `getMealsByDate(invalidDate) should handle invalid date format gracefully`() {
        // Given
        val invalidDate = "invalid-date"
        val allMeals = listOf<Meal>() // Empty list

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getMealsByDateUseCase.getMealsByDate(invalidDate)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).isEmpty()
        }
    }


    @Test
    fun `getMealsByDate() should handle failure of getDateFromString in isMealWithDate when invalidDate`() {
        // Given
        val invalidDate = "invalid-date"
        val meal = buildMeal(id = 1, submitted = Date()) // A meal with any date

        // Mock the repository to return a list with the meal
        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(invalidDate)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).isEmpty() // No meals should be returned due to the failure
        }

//        TODO: verify calling the get function once
//        TODO: verify list size
    }

    @Test
    fun `getMealsByDate(date) should handle meals when null submitted dates`() {
        // Given
        val date = "2023-10-01"
        val validMeal = buildMeal(id = 1, submitted = getDateFromString(date).getOrThrow())
        val nullMeal = buildMeal(id = 2, submitted = null) // Meal with null submitted date

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(validMeal, nullMeal))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).containsExactly(validMeal)
        }
    }

    @Test
    fun `getMealsByDate(date) should sort meals by id correctly`() {
        // Given
        val date = "2023-10-01"
        val utilDate = getDateFromString(date).getOrThrow()
        val meal1 = buildMeal(id = -1, submitted = utilDate)
        val meal2 = buildMeal(id = 10, submitted = utilDate)
        val meal3 = buildMeal(id = 5, submitted = utilDate)

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal2, meal3, meal1))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).containsExactly(meal1, meal3, meal2).inOrder()
        }
    }


    @Test
    fun `getMealsByDate(date) should handle timezone differences in submitted dates`() {
        // Given
        val date = "2023-10-01"
        val utilDate = getDateFromString(date).getOrThrow()
        val mealWithTimeZone = buildMeal(id = 1, submitted = Date(utilDate.time + 3600 * 1000)) // Add 1 hour

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(mealWithTimeZone))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).isEmpty()
        }
    }

}