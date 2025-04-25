package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetMealsByDateUseCase
import org.example.utils.getDateFromString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.buildMeal
import java.util.*

class GetMealsByDateUseCaseTest {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var getMealsByDateUseCase: GetMealsByDateUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk(relaxed = true)
        getMealsByDateUseCase = GetMealsByDateUseCase(mealsRepository)
    }

    @AfterEach
    fun teardown(){
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }

    @Test
    fun `getMealsByDate should return meals filtered by date when given valid date`() {
        // Given
        val date = "2025-04-23"
        val utilDate = getDateFromString(date).getOrThrow()
        val meal1 = buildMeal(id = 1, submitted = utilDate)
        val meal2 = buildMeal(id = 2, submitted = Date())
        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1, meal2))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        assertThat(result.getOrThrow()).containsExactly(meal1)
    }

    @Test
    fun `getMealsByDate should return empty list when no meals match the given date`() {
        // Given
        val date = "2025-04-23"
        val meal1 = buildMeal(id = 1, submitted = Date())
        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        assertThat(result.getOrThrow()).isEmpty()
    }

    @Test
    fun `getMealsByDate should propagate failure when repository fails to get meals`() {
        // Given
        val date = "2025-04-23"
        val exception = Exception("Database error")
        every { mealsRepository.getAllMeals() } returns Result.failure(exception)

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `getMealsByDate should return empty list when given invalid date format`() {
        // Given
        val invalidDate = "invalid-date"
        every { mealsRepository.getAllMeals() } returns Result.success(emptyList())

        // When
        val result = getMealsByDateUseCase.getMealsByDate(invalidDate)

        // Then
        assertThat(result.getOrThrow()).isEmpty()
    }

    @Test
    fun `getMealsByDate should handle date parsing failure when given invalid date format`() {
        // Given
        val invalidDate = "invalid-date"
        val meal = buildMeal(id = 1, submitted = Date())
        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(invalidDate)

        // Then
        assertThat(result.getOrThrow()).isEmpty()
    }

    @Test
    fun `getMealsByDate should exclude meals when they have null submitted dates`() {
        // Given
        val date = "2023-10-01"
        val validMeal = buildMeal(id = 1, submitted = getDateFromString(date).getOrThrow())
        val nullMeal = buildMeal(id = 2, submitted = null)
        every { mealsRepository.getAllMeals() } returns Result.success(listOf(validMeal, nullMeal))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        assertThat(result.getOrThrow()).containsExactly(validMeal)
    }

    @Test
    fun `getMealsByDate should return meals sorted by id when multiple meals match the date`() {
        // Given
        val date = "2023-10-01"
        val utilDate = getDateFromString(date).getOrThrow()
        val meal1 = buildMeal(id = -1, submitted = utilDate)
        val meal2 = buildMeal(id = 10, submitted = utilDate)
        val meal3 = buildMeal(id = 5, submitted = utilDate)
        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal2, meal3, meal1))

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        assertThat(result.getOrThrow()).containsExactly(meal1, meal3, meal2).inOrder()
    }

    @Test
    fun `getMealsByDate should throw when repository fails`() {
        // Given
        val date = "2025-04-23"
        val exception = Exception("Database error")
        every { mealsRepository.getAllMeals() } returns Result.failure(exception)

        // When
        val result = getMealsByDateUseCase.getMealsByDate(date)

        // Then
        val thrown = assertThrows<Throwable> { result.getOrThrow() }
        assertThat(thrown).isEqualTo(exception)
    }
}