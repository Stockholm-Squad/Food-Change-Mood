package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetCountryMealsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.buildMeal

class GetCountryMealsUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var getCountryMealsUseCase: GetCountryMealsUseCase

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk()
        getCountryMealsUseCase = GetCountryMealsUseCase(mealsRepository)
    }

    @Test
    fun `getMealsForCountry() should return true when country name is valid`() {
        // Given
        val meal1 = buildMeal(
            id = 1, name = "emotional balance  spice mixture", tags = listOf("asian", "indian")
        )
        val meal2 = buildMeal(
            id = 2, name = "i stole the idea from mirj  sesame noodles", tags = listOf("asian")
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1, meal2))
        //When
        val result = getCountryMealsUseCase.getMealsForCountry("asian")
        //Then

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `getMealsForCountry() should return false when no meals for that country is valid`() {
        // Given
        val meal1 = buildMeal(
            id = 1, name = "emotional balance  spice mixture", tags = listOf("egypt", "indian")
        )
        val meal2 = buildMeal(
            id = 2, name = "i stole the idea from mirj  sesame noodles", tags = listOf("cairo")
        )
        every { mealsRepository.getAllMeals() } returns Result.success(
            listOf(meal1, meal2)
        )
        //When
        val result = getCountryMealsUseCase.getMealsForCountry("asia")
        //Then
        assertThat(result.isFailure).isTrue()

    }

    @Test
    fun `getMealsForCountry() should return false when countryName in empty is valid`() {
        // Given
        val meal1 = buildMeal(
            id = 1, name = "emotional balance  spice mixture", tags = listOf("egypt", "indian")
        )
        val meal2 = buildMeal(
            id = 2, name = "i stole the idea from mirj  sesame noodles", tags = listOf("cairo")
        )
        every { mealsRepository.getAllMeals() } returns Result.success(
            listOf(meal1, meal2)
        )
        //When
        val result = getCountryMealsUseCase.getMealsForCountry("")
        //Then
        assertThat(result.isFailure).isTrue()

    }

    @Test
    fun `getMealsForCountry() should return failure when repository returns failure`() {
        //Given
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable())

        //Then
        assertThrows<Throwable> { getCountryMealsUseCase.getMealsForCountry("asia").getOrThrow() }
    }

    @Test
    fun `getMealsForCountry() should return the meals size`() {
        // Given
        val meal1 = buildMeal(
            id = 1, name = "emotional balance  spice mixture", tags = listOf("asian", "indian")
        )
        val meal2 = buildMeal(
            id = 2, name = "i stole the idea from mirj  sesame noodles", tags = listOf("cairo")
        )
        val meal3 = buildMeal(
            id = 3, tags = listOf("asian")
        )
        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1, meal2, meal3))
        // When
        val result = getCountryMealsUseCase.getMealsForCountry("asian")
        //Then
        assertThat(result.getOrNull()?.size).isEqualTo(2)
    }


    @Test
    fun `getMealsForCountry() should return the meals size ignoring Upper and Lower case`() {
        // Given
        val meal1 = buildMeal(
            id = 1, name = "emotional balance  spice mixture", tags = listOf("AsIan", "indian")
        )
        val meal2 = buildMeal(
            id = 2, name = "i stole the idea from mirj  sesame noodles", tags = listOf("cairo")
        )
        val meal3 = buildMeal(
            id = 3, tags = listOf("ASian")
        )
        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1, meal2, meal3))
        // When
        val result = getCountryMealsUseCase.getMealsForCountry("asian")
        //Then
        assertThat(result.getOrNull()?.size).isEqualTo(2)
    }

    @Test
    fun `getMealsForCountry() should return the meal when in the name not the tags`() {
        // Given
        val meal1 = buildMeal(
            id = 1, name = "asian spice mixture", tags = listOf("indian")
        )

        every { mealsRepository.getAllMeals() } returns Result.success(listOf(meal1))
        // When
        val result = getCountryMealsUseCase.getMealsForCountry("asian")
        //Then
        assertThat(result.getOrThrow()).containsExactly(meal1)
    }
}