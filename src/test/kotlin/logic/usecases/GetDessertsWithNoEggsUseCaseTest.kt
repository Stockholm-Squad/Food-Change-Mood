package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetDessertsWithNoEggsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.buildMeal

class GetDessertsWithNoEggsUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var getDessertsWithNoEggsUseCase: GetDessertsWithNoEggsUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk()
        getDessertsWithNoEggsUseCase = GetDessertsWithNoEggsUseCase(mealsRepository)
    }

    @Test
    fun `getDessertsWithNNoEggs() should return success when there are desserts with no eggs`() {
        //Given
        val meal1 = buildMeal(
            id = 1,
            tags = listOf("dessert"),
            ingredients = listOf("flour", "sugar", "eggs")
        )
        val meal2 = buildMeal(
            id = 2,
            tags = listOf("dessert"),
            ingredients = listOf("milk", "vanilla")
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1, meal2))
        //When
        val result = getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs()
        //Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.size).isEqualTo(1)
    }

    @Test
    fun `getDessertsWithNNoEggs() should return failure when all desserts contain eggs`() {
        //Given
        val meal = buildMeal(
            id = 1,
            tags = listOf("dessert"),
            ingredients = listOf("egg", "sugar")
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal))
        //When
        val result = getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs()
        //Then
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `getDessertsWithNNoEggs() should return failure when there are no desserts at all`() {
        //Given
        val meal = buildMeal(
            id = 1,
            tags = listOf("breakfast"),
            ingredients = listOf("egg", "toast")
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal))
        //When
        val result = getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs()
        //Then
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `getDessertsWithNNoEggs() should return failure when repository returns failure`() {
        //Given
        every { mealsRepository.getAllMeals() } returns Result.failure(RuntimeException("Repo error"))
        //When
        val result = getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs()
        //Then
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `getDessertsWithNNoEggs()should return failure when meals list is empty`() {
        // Given
        every { mealsRepository.getAllMeals() } returns Result.success(emptyList())

        // When
        val result = getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs()

        // Then
        assertThat(result.isFailure).isTrue()
    }

}
