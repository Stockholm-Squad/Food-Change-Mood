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
import kotlin.test.Test

//success
// no meals found
// no protein level
// protein levels ranked from high to low
//multiple seafood with same level
// get from repo failed
// when meals has one with no seafood
class GetSeaFoodByProteinRankUseCaseTest() {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var getSeaFoodByProteinRankUseCase: GetSeaFoodByProteinRankUseCase

    @BeforeEach
    fun setup() {
        mealsRepository = mockk(relaxed = true)
        getSeaFoodByProteinRankUseCase = GetSeaFoodByProteinRankUseCase(mealsRepository)
    }

    @AfterEach
    fun verifyRepoCalls() {
        verify(exactly = 1) { mealsRepository.getAllMeals() }
    }
    //Paramatrized 
    @Test
    fun `getSeaFoodByProteinRank() should return ranked seafood meals by protein when called`() {
        //Given
        val meals = SeaFoodMeals.getAllMeals()
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
    fun `getSeaFoodByProteinRank() should return failure when meals repository returns NoSuchElementException`() {
        //Given
        every { mealsRepository.getAllMeals() } returns Result.failure(NoSuchElementException())
        //When
        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        //Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(NoSuchElementException()::class.java)
    }

    @Test
    fun `getSeaFoodByProteinRank() should return seafood meals ranked by protein and ignore meals with null protein`() {
        //Given
        val meals = SeaFoodMeals.getAllMealsContainsMealWithNullProtein()
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
        val meals = SeaFoodMeals.getAllMealsWithMealsWithSameProteinLevel()
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
        val meals = SeaFoodMeals.getAllMealsWithOneWithNoSeaFood()
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
    fun `getSeaFoodByProteinRank() should return failure when meals repo returns failure`(){
        every { mealsRepository.getAllMeals() } returns Result.failure(Throwable())

        val result = getSeaFoodByProteinRankUseCase.getSeaFoodByProteinRank()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(Throwable::class.java)
        //get or throwable
    }

}