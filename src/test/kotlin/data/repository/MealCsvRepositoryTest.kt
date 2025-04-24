package data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Meal
import org.example.data.dataSource.MealDataSource
import org.example.data.repository.MealCsvRepository
import org.example.logic.repository.MealsRepository
import org.junit.jupiter.api.*
import utils.buildMeal

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class MealCsvRepositoryTest {
    private lateinit var mealDataSource: MealDataSource
    private lateinit var repository: MealsRepository

    @BeforeEach
    fun setUp() {
        mealDataSource = mockk(relaxed = true)
        repository = MealCsvRepository(mealDataSource)
    }

    @Test
    @Order(1)
    fun `getAllMeals() should return failure result with exception when there is no cached data and data source returns failure result`() {
        //Given
        every {
            mealDataSource.getAllMeals()
        } returns Result.failure(Throwable())
        //resetAllMeals()

        //When
        val result = repository.getAllMeals()

        //Then
        assertThrows<Throwable> { result.getOrThrow() }
    }

    @Test
    @Order(2)
    fun `getAllMeals() should return success result with empty list when there is no cached data and data source returns success result with empty list`() {
        //Given
        val meals = emptyList<Meal>()
        every {
            mealDataSource.getAllMeals()
        } returns Result.success(meals)

        //When
        val result = repository.getAllMeals()

        //Then
        assertThat(result.getOrThrow()).isEqualTo(meals)
    }

    @Test
    @Order(3)
    fun `getAllMeals() should return success result with list of meals when there is no cached data and data source returns success result with list of meals`() {
        //Given
        val meals = listOf(
            buildMeal(1), buildMeal(2), buildMeal(3),
        )
        every {
            mealDataSource.getAllMeals()
        } returns Result.success(meals)

        //resetAllMeals()

        //When
        val result = repository.getAllMeals()

        //Then
        assertThat(result.getOrThrow()).isEqualTo(meals)
    }

    @Test
    @Order(3)
    fun `getAllMeals() should return list of cashed meals when there is a cashed meals`() {
        //Given
        val meals = listOf(
            buildMeal(1), buildMeal(2), buildMeal(3),
        )

        //When
        val result = repository.getAllMeals()

        //Then
        assertThat(result.getOrThrow()).isEqualTo(meals)
    }
}