package utils

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.example.utils.DateParser
import org.example.utils.DateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DateValidatorTest {
    private lateinit var dateValidator: DateValidator

    private lateinit var dateParser: DateParser
    @BeforeEach
    fun setUp() {
        dateParser = mockk(relaxed = true)
        dateValidator = DateValidator(dateParser)
    }

    @Test
    fun `isValidDate() should return false when invalid date`() {
        //Given
        val invalidDate = "2025-02-30"
        every { dateParser.getDateFromString(invalidDate) } returns Result.failure(Throwable())
        //When
        val result = dateValidator.isValidDate(invalidDate)

        //Then
        assertThat(result).isFalse()
    }

    @Test
    fun `isValidDate() should return true when valid date`() {
        val validDate = "2025-05-01"
        every { dateParser.getDateFromString(validDate) } returns Result.success(LocalDate(2025,5,1))
        //When
        val result = dateValidator.isValidDate(validDate)

        //Then
        assertThat(result).isTrue()
    }
}