package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetDessertsWithNoEggsUseCase
import org.example.model.FoodChangeMoodExceptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.buildMeal

class GetDessertsWithNoEggsUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var getDessertsWithNoEggsUseCase: GetDessertsWithNoEggsUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk()
        getDessertsWithNoEggsUseCase = GetDessertsWithNoEggsUseCase(mealsRepository)
    }

    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { mealsRepository.getAllMeals() }
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

//        assertThat(result.isSuccess).isTrue()
//        assertThat(result.getOrNull()?.size).isEqualTo(1)
        assertThat(result.getOrThrow()).containsExactly(meal2)
        //   assertThat(result.getOrThrow()).isEqualTo(listOf(meal2))
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
        assertThrows<FoodChangeMoodExceptions.LogicException.NoDessertFound> { result.getOrThrow() }
    }


    @Test
    fun `getDessertsWithNNoEggs() should return failure when repository returns failure`() {
        //Given
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable())

        //Then
        assertThrows<Throwable> { getDessertsWithNoEggsUseCase.getDessertsWithNNoEggs().getOrThrow() }
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
