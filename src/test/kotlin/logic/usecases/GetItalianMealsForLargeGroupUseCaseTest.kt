package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.usecases.utils.buildMeal
import model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetItalianMealsForLargeGroupUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetItalianMealsForLargeGroupUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var getItalianMealsForLargeGroupUseCase: GetItalianMealsForLargeGroupUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk(relaxed = true)
        getItalianMealsForLargeGroupUseCase = GetItalianMealsForLargeGroupUseCase(mealsRepository)
    }

    @Test
    fun `getMeals() should return Italian meals for large groups`() {
        // Given
        val meal1 = buildMeal(id = 1, tags = listOf("italian", "for-large-groups"))
        val meal2 = buildMeal(id = 2, tags = listOf("mexican", "for-large-groups")) // Not Italian
        val meal3 = buildMeal(id = 3, tags = listOf("italian")) // Missing "for-large-groups"
        val allMeals = listOf(meal1, meal2, meal3)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).containsExactly(meal1) // Only meal1 matches both tags
        }
    }

    @Test
    fun `getMeals() should return empty list when no meals match the criteria`() {
        // Given
        val meal1 = buildMeal(id = 1, tags = listOf("mexican", "for-large-groups")) // Not Italian
        val meal2 = buildMeal(id = 2, tags = listOf("italian")) // Missing "for-large-groups"
        val allMeals = listOf(meal1, meal2)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).isEmpty() // No meals match both tags
        }
    }


    @Test
    fun `getMeals() should handle meals with null tags gracefully`() {
        // Given
        val meal1 = buildMeal(id = 1, tags = null) // Null tags
        val meal2 = buildMeal(id = 2, tags = listOf("italian", "for-large-groups"))
        val allMeals = listOf(meal1, meal2)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).containsExactly(meal2) // Only meal2 has valid tags
        }
    }

    @Test
    fun `getMeals() should sort meals by id correctly`() {
        // Given
        val meal1 = buildMeal(id = 3, tags = listOf("italian", "for-large-groups"))
        val meal2 = buildMeal(id = 1, tags = listOf("italian", "for-large-groups"))
        val meal3 = buildMeal(id = 2, tags = listOf("italian", "for-large-groups"))
        val allMeals = listOf(meal1, meal2, meal3)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).containsExactly(meal2, meal3, meal1).inOrder() // Sorted by ID
        }
    }

    @Test
    fun `getMeals() should handle empty repository response gracefully`() {
        // Given
        val allMeals = emptyList<Meal>()

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { meals ->
            assertThat(meals).isEmpty() // Empty list should be returned
        }
    }

    @Test
    fun `getMeals() should propagate failure when repository fails`() {
        // Given
        val exception = Exception("Database error")

        // Mock the repository to return a failure
        every { mealsRepository.getAllMeals() } returns Result.failure(exception)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.isFailure).isTrue()
        result.onFailure { cause ->
            assertThat(cause).isEqualTo(exception) // Ensure the same exception is propagated
        }
    }
}