package logic.usecases

import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetMealByNameUseCase
import org.example.logic.usecases.SearchingByKmpUseCase
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class GetMealByNameUseCaseTest {
// add given /when / then
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
        val matchingMeal = listOf(
            meal(name = "5 minute bread pizza"),
            meal(name = "pizza wrap")
        )
        val meals = listOf(
            meal(name = "5 minute bread pizza"),
            meal(name = "ww core polenta crust pizza"),
            meal(name = "pizza wrap"),
            meal(name = "bbq meatballs   egg noodles"),
            meal(name = "bread   butter pickle deviled eggs")
        )
        val pattern = "piz"

        every { mealsRepository.getAllMeals() } returns Result.success(meals)
        every { searchingByKmpUseCase.searchByKmp("5 minute bread pizza", pattern) } returns true
        every { searchingByKmpUseCase.searchByKmp("pizza wrap", pattern) } returns true
        every { searchingByKmpUseCase.searchByKmp("ww core polenta crust pizza", pattern) } returns false
        every { searchingByKmpUseCase.searchByKmp("bbq meatballs   egg noodles", pattern) } returns false
        every { searchingByKmpUseCase.searchByKmp("bread   butter pickle deviled eggs", pattern) } returns false

        val result = getMealByNameUseCase.getMealByName(pattern)

        // truth
        assertTrue(result.isSuccess)
        assertEquals(
            matchingMeal.map { it.name },
            result.getOrNull()?.map { it.name }
        )
    }

    @Test
    fun `should return failure when no meals match the pattern`() {
        val meals = listOf(
            meal(name = "bbq meatballs   egg noodles"),
            meal(name = "bread   butter pickle deviled eggs")
        )
        val pattern = "piz"


        every { mealsRepository.getAllMeals() } returns Result.success(meals)
        every { searchingByKmpUseCase.searchByKmp("bbq meatballs   egg noodles", pattern) } returns false
        every { searchingByKmpUseCase.searchByKmp("bread   butter pickle deviled eggs", pattern) } returns false

        val result = getMealByNameUseCase.getMealByName(pattern)
// truth
        assertFalse(result.isSuccess)
        assertEquals(Constants.NO_MEALS_FOUND_MATCHING, result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when pattern is blank`() {
        val result = getMealByNameUseCase.getMealByName("   ")
        assertTrue(result.isFailure)
        assertEquals(Constants.SEARCH_QUERY_CAN_NOT_BE_EMPTY, result.exceptionOrNull()?.message)
    }

    @Test
    fun `should return failure when repository fails`() {
        val pattern = "piz"
        val repositoryErrorMessage = "Csv.file failed"
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable(repositoryErrorMessage))

        val result = getMealByNameUseCase.getMealByName(pattern)
        assertTrue(result.isFailure)

        val exception = result.exceptionOrNull()
        assertNotNull(exception)

        val errorMessage = exception.message
        assertNotNull(errorMessage)

        assertTrue(errorMessage.contains(Constants.ERROR_FETCHING_MEALS))
        // throwable truth
        assertTrue(errorMessage.contains(repositoryErrorMessage))
    }
}
