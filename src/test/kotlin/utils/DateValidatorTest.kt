package utils

import com.google.common.truth.Truth.assertThat
import org.example.utils.DateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DateValidatorTest {
    private lateinit var dateValidator: DateValidator

    @BeforeEach
    fun setUp() {
        dateValidator = DateValidator()
    }

    @Test
    fun `isValidDate() should return false when invalid date`() {
        //Given
        val invalidDate = "2025-02-30"

        //When
        val result = dateValidator.isValidDate(invalidDate)

        //Then
        assertThat(result).isFalse()
    }

    @Test
    fun `isValidDate() should return true when valid date`() {
        val validDate = "2025-05-01"
        val result = dateValidator.isValidDate(validDate)
        assertThat(result).isTrue()
    }
}