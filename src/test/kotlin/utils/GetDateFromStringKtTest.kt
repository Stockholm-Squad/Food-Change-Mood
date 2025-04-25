package utils

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.example.model.FoodChangeMoodExceptions
import org.example.utils.getDateFromString
import org.junit.jupiter.api.Assertions.*

import kotlin.test.Test

class GetDateFromStringKtTest {
    @Test
    fun `should return LocalDate when valid date string is given`() {
        val input = "2024-04-25"
        val result = getDateFromString(input)
        assertThat(result).isEqualTo(LocalDate(2024,4,5))
    }

    @Test
    fun `should return failure when invalid date string is given`() {
        val input = "invalid-date"
        val result = getDateFromString(input)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is FoodChangeMoodExceptions.LogicException.CanNotParseDateFromString)
    }

    @Test
    fun `should return failure when empty string is given`() {
        val input = ""
        val result = getDateFromString(input)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is FoodChangeMoodExceptions.LogicException.CanNotParseDateFromString)
    }

    @Test
    fun `should return failure when date is in wrong format`() {
        val input = "25-04-2024" // expecting yyyy-MM-dd but got dd-MM-yyyy
        val result = getDateFromString(input)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is FoodChangeMoodExceptions.LogicException.CanNotParseDateFromString)
    }
}