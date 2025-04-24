package logic.usecases.getseafoodmealsbyproteintest

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import utils.buildMeal
import utils.buildNutrition
import kotlin.test.Test

class GetSeaFoodByProteinRankUseCaseTest() {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase

    @BeforeEach
    fun setup() {
        mealsRepository = mockk(relaxed = true)
        getSeaFoodByProteinRankUseCase = GetSeaFoodByProteinRankUseCase(mealsRepository)
    }

    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }

    @Test
    fun `getSeaFoodByProteinRank() should return ranked seafood meals by protein when called`() {
        //Given
        val meals = getSeaFoodMeals()
        every { mealsRepository.getAllMeals() } returns Result.success(meals)
        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        val seafoodResult = result.getOrNull()
        //Then
        assertThat(seafoodResult?.get(0)?.name).isEqualTo("Pan fried fish")
        assertThat(seafoodResult?.get(1)?.name).isEqualTo("Shrimp pasta")
        assertThat(seafoodResult?.get(2)?.name).isEqualTo("Veggie salad")

    }

    @Test
    fun `getSeaFoodByProteinRank() should return seafood meals ranked by protein and ignore meals with null protein`() {
        //Given
        val meals = getSeaFoodMeals() + buildMeal(
            id = 4,
            name = "Shrimp pasta",
            description = "A tasty seafood pasta",
            nutrition = buildNutrition(protein = null)
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)
        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        val seafoodResult = result.getOrNull()
        assertThat(seafoodResult?.size).isEqualTo(3)
        assertThat(seafoodResult?.get(0)?.name).isEqualTo("Pan fried fish")
        assertThat(seafoodResult?.get(1)?.name).isEqualTo("Shrimp pasta")
        assertThat(seafoodResult?.get(2)?.name).isEqualTo("Veggie salad")
    }

    @Test
    fun `getSeaFoodByProteinRank() should return seafood meals sorted by protein and name when protein values match`() {
        //Given
        val meals = getSeaFoodMeals() + buildMeal(
            id = 3,
            name = "Vaggie salad",
            description = "Fresh salad with no seafood",
            nutrition = buildNutrition(protein = 5f)
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)
        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        val seafoodResult = result.getOrNull()
        assertThat(seafoodResult?.size).isEqualTo(4)
        assertThat(seafoodResult?.get(0)?.name).isEqualTo("Pan fried fish")
        assertThat(seafoodResult?.get(1)?.name).isEqualTo("Shrimp pasta")
        assertThat(seafoodResult?.get(2)?.name).isEqualTo("Vaggie salad")
        assertThat(seafoodResult?.get(3)?.name).isEqualTo("Veggie salad")
    }

    @Test
    fun `getSeaFoodByProteinRank() should return seafood meals sorted by protein ignoring non-seafood meals`() {
        //Given
        val meals = getSeaFoodMeals() + buildMeal(
            id = 4,
            name = "pasta",
            description = "A tasty pasta",
            nutrition = buildNutrition(protein = 20f)
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)
        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        val seafoodResult = result.getOrNull()
        //Then
        assertThat(seafoodResult?.size).isEqualTo(3)
        assertThat(seafoodResult?.get(0)?.name).isEqualTo("Pan fried fish")
        assertThat(seafoodResult?.get(1)?.name).isEqualTo("Shrimp pasta")
        assertThat(seafoodResult?.get(2)?.name).isEqualTo("Veggie salad")
    }

    @Test
    fun `getSeaFoodByProteinRank() should return failure when meals repo returns failure`() {
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable())
        assertThrows<Throwable> {
            getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank().getOrThrow()
        }
    }

}