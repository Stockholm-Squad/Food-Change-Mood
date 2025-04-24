package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.verify
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetMealByNameUseCase
import org.example.logic.usecases.SearchingByKmpUseCase
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import utils.buildMeal
import kotlin.test.Test


class GetMealByNameUseCaseTest {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var getMealByNameUseCase: GetMealByNameUseCase
    private lateinit var searchingByKmpUseCase: SearchingByKmpUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk(relaxed = true)
        searchingByKmpUseCase = mockk(relaxed = true)
        getMealByNameUseCase = GetMealByNameUseCase(mealsRepository, searchingByKmpUseCase)
    }

    @Test
    fun `should return meal when pattern is found in meal name`() {

        // Given
        val matchingMeal = listOf(
            buildMeal(name = "5 minute bread pizza", id = 1),
            buildMeal(name = "pizza wrap", id = 2)
        )

        val allMeals = listOf(
            buildMeal(name = "5 minute bread pizza", id = 1),
            buildMeal(name = "ww core polenta crust pizza", id = 2),
            buildMeal(name = "pizza wrap", id = 3),
            buildMeal(name = "bbq meatballs   egg noodles", id = 4),
            buildMeal(name = "bread   butter pickle deviled eggs", id = 5)
        )

        val pattern = "piz"

        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)
        every { searchingByKmpUseCase.searchByKmp("5 minute bread pizza", pattern) } returns true
        every { searchingByKmpUseCase.searchByKmp("pizza wrap", pattern) } returns true
        every { searchingByKmpUseCase.searchByKmp("ww core polenta crust pizza", pattern) } returns false
        every { searchingByKmpUseCase.searchByKmp("bbq meatballs   egg noodles", pattern) } returns false
        every { searchingByKmpUseCase.searchByKmp("bread   butter pickle deviled eggs", pattern) } returns false

        // when
        val result = getMealByNameUseCase.getMealByName(pattern)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.map { it.name }).isEqualTo(matchingMeal.map { it.name })

        // And
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }

    @Test
    fun `should return failure when no meals match the pattern`() {

        // Given
        val allMeals = listOf(
            buildMeal(name = "bbq meatballs   egg noodles", id = 1),
            buildMeal(name = "bread   butter pickle deviled eggs", id = 2)
        )
        val pattern = "piz"


        every { mealsRepository.getAllMeals() } returns Result.success(allMeals)
        every { searchingByKmpUseCase.searchByKmp("bbq meatballs   egg noodles", pattern) } returns false
        every { searchingByKmpUseCase.searchByKmp("bread   butter pickle deviled eggs", pattern) } returns false

        // When
        val result = getMealByNameUseCase.getMealByName(pattern)

        // Then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.exceptionOrNull()?.message).isEqualTo(Constants.NO_MEALS_FOUND_MATCHING)

        // And
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }

    @Test
    fun `should return failure when pattern is blank`() {

        // When
        val result = getMealByNameUseCase.getMealByName("   ")

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY)

        // And
        verify(exactly = 0) { mealsRepository.getAllMeals() }
    }

    @Test
    fun `should return failure when repository fails`() {

        // Given
        val pattern = "piz"
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable())

        // When
        val result = getMealByNameUseCase.getMealByName(pattern)

        // Then
        assertThat(result.isFailure).isTrue()

        // And
        val exception = result.exceptionOrNull()
        assertThat(exception).isNotNull()

        val errorMessage = exception?.message
        assertThat(errorMessage).isNotNull()

        assertThat(errorMessage).contains(Constants.ERROR_FETCHING_MEALS)

        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }

}
