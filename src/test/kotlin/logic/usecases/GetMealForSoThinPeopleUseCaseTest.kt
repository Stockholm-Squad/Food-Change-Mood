package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetMealForSoThinPeopleUseCase
import org.example.model.FoodChangeMoodExceptions.LogicException.NoMealsForSoThinPeopleException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.buildMeal
import utils.buildNutrition

class GetMealForSoThinPeopleUseCaseTest {
    private lateinit var repository: MealsRepository
    private lateinit var getMealForSoThinPeople: GetMealForSoThinPeopleUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        getMealForSoThinPeople = GetMealForSoThinPeopleUseCase(repository)
    }

    @Test
    fun `suggestRandomMealForSoThinPeople() should return meal when have meal with more than 700 calories`() {
        // given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                buildMeal(1, nutrition = buildNutrition(calories = 10f)),
                buildMeal(3, nutrition = buildNutrition(12f)),
                buildMeal(4, nutrition = buildNutrition(400f)),
                buildMeal(5, nutrition = buildNutrition(5000f))
            )
        )
        // when
        val result = getMealForSoThinPeople.suggestRandomMealForSoThinPeople()

        // then
        assertThat(result.isSuccess).isNotNull()
    }

    @Test
    fun `suggestRandomMealForSoThinPeople()  should return different meal when called more than once`() {
        // given

        every { repository.getAllMeals() } returns Result.success(
            listOf(
                buildMeal(1, nutrition = buildNutrition(calories = 10f)),
                buildMeal(3, nutrition = buildNutrition(1200f)),
                buildMeal(4, nutrition = buildNutrition(400f)),
                buildMeal(5, nutrition = buildNutrition(5000f))
            )
        )
        // when
        val firstCallResult = getMealForSoThinPeople.suggestRandomMealForSoThinPeople()
        val secondCallResult = getMealForSoThinPeople.suggestRandomMealForSoThinPeople()
        // then
        assertThat(firstCallResult).isNotEqualTo(secondCallResult)
    }

    @Test
    fun `suggestRandomMealForSoThinPeople() should throw when the nutrition is null `() {
        // given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                buildMeal(1, nutrition = null)
            )
        )
        // when
        val result = getMealForSoThinPeople.suggestRandomMealForSoThinPeople()
        // then
        assertThrows<NoMealsForSoThinPeopleException> { result.getOrThrow() }
    }

    @Test
    fun `suggestRandomMealForSoThinPeople() should throw when calories are null`() {
        // given
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                buildMeal(1, nutrition = buildNutrition(null, 20f, 30f))
            )
        )
        // when
        val result = getMealForSoThinPeople.suggestRandomMealForSoThinPeople()

        // then
        assertThrows<NoMealsForSoThinPeopleException> { result.getOrThrow() }
    }

    @Test
    fun `suggestRandomMealForSoThinPeople() should throw when are no meals`() {
        // given
        every { repository.getAllMeals() } returns Result.failure(
            Throwable()
        )
        // when
        val result = getMealForSoThinPeople.suggestRandomMealForSoThinPeople()

        // then
        assertThrows<Throwable> { result.getOrThrow() }
    }


}
