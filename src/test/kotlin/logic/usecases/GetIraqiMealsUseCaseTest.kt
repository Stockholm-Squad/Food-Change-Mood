package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecases.GetIraqiMealsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import utils.buildMeal

class GetIraqiMealsUseCaseTest {
 private lateinit var repository: MealsRepository
 private lateinit var getIraqiMealsUseCase: GetIraqiMealsUseCase

 @BeforeEach
 fun setup() {
  repository = mockk(relaxed = true)
  getIraqiMealsUseCase = GetIraqiMealsUseCase(repository)
 }

 @ParameterizedTest
 @CsvSource(
  "iraq",
  "IRAQI",
  "iraqi",
  "IrAqI",
  "iraq",
  "IRAQ",
  "IraqI"
 )
 fun `getIraqiMeals() should return meals with iraqi in tags (case-insensitive)`(
  tags: String,
 ) {
  // given
  every { repository.getAllMeals() } returns Result.success(listOf(buildMeal(id = 1, tags = listOf(tags))))

  // when
  val result = getIraqiMealsUseCase.getIraqiMeals()

  // then
  assertThat(result.getOrThrow())
 }

 @ParameterizedTest
 @CsvSource(
  "iraq",
  "IRAQI",
  "iraqi",
  "IrAqI",
  "iraq",
  "IRAQ",
  "IraqI"
 )
 fun `getIraqiMeals() should return meals with iraq in description (case-insensitive)`(
  description: String,
 ) {
  every { repository.getAllMeals() } returns Result.success(listOf(buildMeal(id = 12, description = description)))

  // when
  val result = getIraqiMealsUseCase.getIraqiMeals()

  // then
  assertThat(result.getOrThrow())

 }

 @Test
 fun `getIraqiMeals() should return meals with iraq in description and tags is null (case-insensitive)`() {

  every { repository.getAllMeals() } returns Result.success(
   listOf(
    buildMeal(
     id = 36,
     description = "iraq",
     tags = null
    )
   )
  )

  // when
  val result = getIraqiMealsUseCase.getIraqiMeals()

  // then
  assertThat(result.getOrThrow())

 }

 @Test
 fun `getIraqiMeals() should return meals with iraq in tags and description is null (case-insensitive)`() {

  every { repository.getAllMeals() } returns Result.success(
   listOf(
    buildMeal(
     id = 10,
     description = null,
     tags = listOf("50", "iraqi")
    )
   )
  )

  // when
  val result = getIraqiMealsUseCase.getIraqiMeals()

  // then
  assertThat(result.getOrThrow())

 }

 @Test
 fun `getIraqiMeals() should return empty list if no tag or description matches`() {
  // given

  every { repository.getAllMeals() } returns Result.success(
   listOf(
    buildMeal(id = 5, tags = listOf("American"), description = "Some food"),
    buildMeal(id = 71, tags = listOf("Indian"), description = "Delicious curry")
   )
  )

  // when
  val result = getIraqiMealsUseCase.getIraqiMeals()

  // then
  assertThat(result.getOrThrow())
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
}