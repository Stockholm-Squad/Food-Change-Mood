package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetIraqiMealsUseCase
import org.example.model.FoodChangeMoodExceptions.LogicException.NoIraqiMeals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.buildMeal

class GetIraqiMealsUseCaseTest {
    private lateinit var repository: MealsRepository
    private lateinit var getIraqiMealsUseCase: GetIraqiMealsUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        getIraqiMealsUseCase = GetIraqiMealsUseCase(repository)
    }

    @AfterEach
    fun tearDown() {
        verify(exactly = 1) { repository.getAllMeals() }
    }

    @Test
    fun `getIraqiMeals() should return meals with iraqi in tags with ignore case`(

    ) {
        // given
        val meal = buildMeal(id = 36, description = "any", tags = listOf("IrAqI"))
        every { repository.getAllMeals() } returns Result.success(
            listOf(
                meal
            )
        )

        // when
        val result = getIraqiMealsUseCase.getIraqiMeals()

        // then
        assertThat(result.getOrThrow()).isEqualTo(listOf(meal))
    }

    @Test
    fun `getIraqiMeals() should return meals with iraq in description with ignore case`(
    ) {
        // given
        val meal =
            buildMeal(id = 36, description = "iRaQ", tags = listOf("hh")) // tags do NOT match, but description does
        every { repository.getAllMeals() } returns Result.success(listOf(meal))

        // when
        val result = getIraqiMealsUseCase.getIraqiMeals()

        // then
        assertThat(result.getOrThrow()).isEqualTo(listOf(meal))

    }

    @Test
    fun `getIraqiMeals() should return meals with iraq in description and tags is null`() {
        // given
        val meal = listOf(
            buildMeal(id = 36, description = "iRaQ", tags = null),
            buildMeal(id = 36, description = "iraq", tags = null)
        )

        every { repository.getAllMeals() } returns Result.success(
            meal
        )
        // when
        val result = getIraqiMealsUseCase.getIraqiMeals()

        // then
        assertThat(result.getOrThrow()).isEqualTo(meal)
    }

    @Test
    fun `getIraqiMeals() should return meals with iraq in tags and description is null `() {
        // given
        val meal = listOf(
            buildMeal(id = 36, description = null, tags = listOf("welcome", "7", "sweet", "iraqi")),
            buildMeal(id = 36, description = " ", tags = listOf("30", "40step", "iRaqi"))
        )
        every { repository.getAllMeals() } returns Result.success(
            meal
        )

        // when
        val result = getIraqiMealsUseCase.getIraqiMeals()

        // then
        assertThat(result.getOrThrow()).isEqualTo(meal)

    }

    @Test
    fun `getIraqiMeals() should return empty list if no tag or description matches`() {
        // given
        val meal = listOf(
            buildMeal(id = 5, tags = listOf("American"), description = "Some food"),
            buildMeal(id = 71, tags = listOf("Indian"), description = "Delicious curry"),
            buildMeal(id = 13, tags = listOf("30"), description = "this")
        )
        every { repository.getAllMeals() } returns Result.success(
            meal
        )

        // when
        val result = getIraqiMealsUseCase.getIraqiMeals()

        // then
        assertThrows<NoIraqiMeals> { result.getOrThrow() }
    }

    @Test
    fun `getIraqiMeals() should throw when no meals`() {
        // given
        every { repository.getAllMeals() } returns Result.failure(Throwable())

        // when
        val result = getIraqiMealsUseCase.getIraqiMeals()

        // then
        assertThrows<Throwable> { result.getOrThrow() }
    }

    @Test
    fun `getIraqiMeals() should throw when both description and tags are null`() {
        // given
        val meal = buildMeal(id = 1, tags = null, description = null)
        every { repository.getAllMeals() } returns Result.success(listOf(meal))
        // when
        val result = getIraqiMealsUseCase.getIraqiMeals()
        // then
        assertThrows<NoIraqiMeals> {
            result.getOrThrow()
        }
    }
}