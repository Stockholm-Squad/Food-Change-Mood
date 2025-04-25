package logic.usecases.getseafoodmealsbyproteintest

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetSeaFoodByProteinRankUseCase
import org.example.model.FoodChangeMoodExceptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import utils.buildMeal
import utils.buildNutrition
import kotlin.test.Test

// ignore case
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
        assertThat(seafoodResult?.size).isEqualTo(3)
        assertThat(seafoodResult).containsExactly(meals[0], (meals[1]), (meals[2])).inOrder()

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

        //Then
        assertThat(seafoodResult?.size).isEqualTo(3)
        assertThat(seafoodResult).containsExactly(meals[0], (meals[1]), (meals[2])).inOrder()
    }

    @Test
    fun `getSeaFoodByProteinRank() should return seafood meals sorted by protein and name when protein values match`() {

        //Given
        val meals = getSeaFoodMeals() + buildMeal(
            id = 3,
            name = "Vzggie salad",
            description = "Fresh salad with seafood",
            nutrition = buildNutrition(protein = 5f)
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        val seafoodResult = result.getOrNull()

        //Then
        assertThat(seafoodResult?.size).isEqualTo(4)
        assertThat(seafoodResult).containsExactly(meals[0], (meals[1]), (meals[2]), meals[3]).inOrder()
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
        assertThat(seafoodResult).containsExactly(meals[0], (meals[1]), (meals[2])).inOrder()
    }

    @Test
    fun `getSeaFoodByProteinRank() should return failure when meals repo returns failure`() {

        //Given
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable())

        //When & Then
        assertThrows<Throwable> {
            getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank().getOrThrow()
        }
    }

    @Test

    fun `getSeaFoodByProteinRank() should return list of meals with any type of case`() {

        // Given
        val meals = listOf(
            buildMeal(1, description = "This is a Seafood dish", nutrition = buildNutrition(protein = 20.0f)),
            buildMeal(2, description = "this is a seafood plate", nutrition = buildNutrition(protein = 320.0f)),
            buildMeal(3, description = "SEAFOOD special", nutrition = buildNutrition(protein = 25.0f)),
            buildMeal(4, description = "Fresh SeaFood cooked well", nutrition = buildNutrition(protein = 15.0f)),
            buildMeal(5, description = "sEaFoOd delight", nutrition = buildNutrition(protein = 18.0f)),
            buildMeal(6, description = "SeAfOoD meal", nutrition = buildNutrition(protein = 22.0f)),
        )
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        val seafoodResult = result.getOrNull()

        //Then
        assertThat(seafoodResult?.size).isEqualTo(6)
        assertThat(seafoodResult).containsExactly(meals[1], (meals[2]), (meals[5]), (meals[0]), (meals[4]), (meals[3]))
            .inOrder()
    }

    @Test
    fun `getSeaFoodByProteinRank() should return list of meals ignore one with description equals null`() {

        //Given
        val meals = getSeaFoodMeals() + buildMeal(id = 4, description = null)
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        val seaFoodResult = result.getOrNull()

        //Then
        assertThat(seaFoodResult?.size).isEqualTo(3)
        assertThat(seaFoodResult).containsExactly(meals[0],meals[1],meals[2]).inOrder()
    }

    @Test
    fun `getSeaFoodByProteinRank() should return list of meals ignore one with nutrition equals null`() {

        //Given
        val meals = getSeaFoodMeals() + buildMeal(id = 4, nutrition = null, description = "seafood")
        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        val seaFoodResult = result.getOrNull()

        //Then
        assertThat(seaFoodResult?.size).isEqualTo(3)
        assertThat(seaFoodResult).containsExactly(meals[0],meals[1],meals[2]).inOrder()
    }


    @Test
    fun `getSeaFoodByProteinRank() should return failure when no seafood meals with protein`() {

        // Given
        val meals = listOf(
            buildMeal(
                id = 1,
                name = "Chicken Salad",
                description = "Fresh chicken with veggies",
                nutrition = buildNutrition(protein = 25f)
            ),
            buildMeal(
                id = 2,
                name = "Fruit Bowl",
                description = "Contains fresh fruits",
                nutrition = buildNutrition(protein = 26f)
            )
        )

        every { mealsRepository.getAllMeals() } returns Result.success(meals)

        //When & Then
        assertThrows<FoodChangeMoodExceptions.LogicException.NoSeaFoodMealsFound> {
            getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank().getOrThrow()
        }
    }

}