package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetItalianMealsForLargeGroupUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.buildMeal

class GetItalianMealsForLargeGroupUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var getItalianMealsForLargeGroupUseCase: GetItalianMealsForLargeGroupUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk(relaxed = true)
        getItalianMealsForLargeGroupUseCase = GetItalianMealsForLargeGroupUseCase(mealsRepository)
    }

    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }

    @Test
    fun `getMeals should return Italian meals for large groups when meals match both tags`() {
        // Given
        val meal1 = buildMeal(id = 1, tags = listOf("italian", "for-large-groups"))
        val meal2 = buildMeal(id = 2, tags = listOf("mexican", "for-large-groups"))
        val meal3 = buildMeal(id = 3, tags = listOf("italian"))
        val allMeals = listOf(meal1, meal2, meal3)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.getOrThrow()).containsExactly(meal1)
    }

    @Test
    fun `getMeals should return empty list when no meals match both required tags`() {
        // Given
        val meal1 = buildMeal(id = 1, tags = listOf("mexican", "for-large-groups"))
        val meal2 = buildMeal(id = 2, tags = listOf("italian"))
        val allMeals = listOf(meal1, meal2)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.getOrThrow()).isEmpty()
    }

    @Test
    fun `getMeals should exclude meals when they have null tags`() {
        // Given
        val meal1 = buildMeal(id = 1, tags = null)
        val meal2 = buildMeal(id = 2, tags = listOf("italian", "for-large-groups"))
        val allMeals = listOf(meal1, meal2)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.getOrThrow()).containsExactly(meal2)
    }

    @Test
    fun `getMeals should return meals sorted by id when multiple meals match criteria`() {
        // Given
        val meal1 = buildMeal(id = 3, tags = listOf("italian", "for-large-groups"))
        val meal2 = buildMeal(id = 1, tags = listOf("italian", "for-large-groups"))
        val meal3 = buildMeal(id = 2, tags = listOf("italian", "for-large-groups"))
        val allMeals = listOf(meal1, meal2, meal3)

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.getOrThrow()).containsExactly(meal2, meal3, meal1).inOrder()
    }

    @Test
    fun `getMeals should return empty list when repository returns empty list`() {
        // Given
        every { mealsRepository.getAllMeals() } returns Result.success(emptyList())

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.getOrThrow()).isEmpty()
    }

    @Test
    fun `getMeals should propagate exception when repository fails`() {
        // Given
        val exception = Exception("Database error")
        every { mealsRepository.getAllMeals() } returns Result.failure(exception)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `getMeals should throw when getOrThrow is called and repository fails`() {
        // Given
        val exception = Exception("Database error")
        every { mealsRepository.getAllMeals() } returns Result.failure(exception)

        // When
        val result = getItalianMealsForLargeGroupUseCase.getMeals()

        // Then
        assertThrows<Throwable> { result.getOrThrow() }
    }
}