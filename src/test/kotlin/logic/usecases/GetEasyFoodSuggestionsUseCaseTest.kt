package logic.usecases

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetEasyFoodSuggestionsUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import utils.buildMeal

class GetEasyFoodSuggestionsUseCaseTest {
    private lateinit var repository: MealsRepository
    private lateinit var getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        getEasyFoodSuggestionsUseCase = GetEasyFoodSuggestionsUseCase(repository)
    }

    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { repository.getAllMeals() }
    }

    @Test
    fun `getEasyFood() should return list of easy meals when repository returns meals matching criteria`() {
        // Given
        val easyMeal1 = buildMeal(1, minutes = 20, numberOfIngredients = 4, numberOfSteps = 5)
        val easyMeal2 = buildMeal(2, minutes = 30, numberOfIngredients = 5, numberOfSteps = 6)
        val complexMeal = buildMeal(3, minutes = 60, numberOfIngredients = 10, numberOfSteps = 10)

        every { repository.getAllMeals() } returns Result.success(
            listOf(easyMeal1, easyMeal2, complexMeal)
        )

        // When
        val result = getEasyFoodSuggestionsUseCase.getEasyFood()

        // Then
        Truth.assertThat(result.getOrThrow()).containsExactly(easyMeal1, easyMeal2)
    }

    @Test
    fun `getEasyFood() should return limited to 10 meals when more than 10 easy meals exist`() {
        // Given
        val easyMeals = (1..15).map {
            buildMeal(it, minutes = 20, numberOfIngredients = 4, numberOfSteps = 5)
        }

        every { repository.getAllMeals() } returns Result.success(easyMeals)

        // When
        val result = getEasyFoodSuggestionsUseCase.getEasyFood()

        // Then
        Truth.assertThat(result.getOrThrow()).hasSize(10)
    }

    @Test
    fun `getEasyFood() should return empty list when no meals match easy criteria`() {
        // Given
        val complexMeal1 = buildMeal(1, minutes = 60, numberOfIngredients = 10, numberOfSteps = 10)
        val complexMeal2 = buildMeal(2, minutes = 40, numberOfIngredients = 8, numberOfSteps = 7)

        every { repository.getAllMeals() } returns Result.success(
            listOf(complexMeal1, complexMeal2)
        )

        // When
        val result = getEasyFoodSuggestionsUseCase.getEasyFood()

        // Then
        assertThrows<NoSuchElementException> { result.getOrThrow() }
    }

    @Test
    fun `getEasyFood() should return failure when repository returns failure`() {
        // Given
        val exception = RuntimeException("Database error")
        every { repository.getAllMeals() } returns Result.failure(exception)

        // When
        val result = getEasyFoodSuggestionsUseCase.getEasyFood()

        // Then
        assertThrows<RuntimeException> { result.getOrThrow() }
    }

    @Test
    fun `getEasyFood() should return only meals that contains value fields when meal have nullable fields`() {
        // Given
        val mealWithNullFields = buildMeal(
            1,
            minutes = null,
            numberOfIngredients = null,
            numberOfSteps = null
        )
        val easyMeal = buildMeal(2, minutes = 20, numberOfIngredients = 4, numberOfSteps = 5)

        every { repository.getAllMeals() } returns Result.success(
            listOf(mealWithNullFields, easyMeal)
        )

        // When
        val result = getEasyFoodSuggestionsUseCase.getEasyFood()

        // Then
        Truth.assertThat(result.getOrThrow()).containsExactly(easyMeal)
    }

    @ParameterizedTest
    @CsvSource(
        "20, 4, 5",    // All values within limits
        "30, 5, 6",    // All values at max limits
        "15, 3, 4"     // All values well within limits
    )
    fun `getEasyFood() should return meal when it meets easy criteria`(
        minutes: Int,
        ingredients: Int,
        steps: Int
    ) {
        // Given
        val testMeal = buildMeal(
            1,
            minutes = minutes,
            numberOfIngredients = ingredients,
            numberOfSteps = steps
        )

        every { repository.getAllMeals() } returns Result.success(listOf(testMeal))

        // When
        val result = getEasyFoodSuggestionsUseCase.getEasyFood()

        // Then
        Truth.assertThat(result.getOrThrow()).isEqualTo(listOf(testMeal))
    }

    @ParameterizedTest
    @CsvSource(
        "31, 5, 6",    // Minutes over limit
        "30, 6, 6",    // Ingredients over limit
        "30, 5, 7",    // Steps over limit
    )
    fun `getEasyFood() should not return meal when it fails easy criteria`(
        minutes: Int?,
        ingredients: Int?,
        steps: Int?
    ) {
        // Given
        val testMeal = buildMeal(
            1,
            minutes = minutes,
            numberOfIngredients = ingredients,
            numberOfSteps = steps
        )

        every { repository.getAllMeals() } returns Result.success(listOf(testMeal))

        // When
        val result = getEasyFoodSuggestionsUseCase.getEasyFood()

        // Then
        assertThrows<NoSuchElementException> { result.getOrThrow() }
    }
}