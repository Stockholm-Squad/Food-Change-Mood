package utils

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.example.utils.DateParserImpl

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DateParserImplTest() {

    private lateinit var dateParserImpl: DateParserImpl

    @BeforeEach
    fun setUp() {
        dateParserImpl = DateParserImpl()
    }

    @Test
    fun `getDateFromString should return failure when gets invalid date`() {
        //Given
        val invalidDate = "2025-02-30"
        //When
        val result = dateParserImpl.getDateFromString(invalidDate)
        //Then
        assertThrows<Throwable> { result.getOrThrow() }

    }

    @Test
    fun `getDateFromString should return date when gets valid date`() {
        //Given
        val validDate = "2025-02-22"
        //When
        val result = dateParserImpl.getDateFromString(validDate)
        //Then
        assertThat(result.getOrThrow()).isEqualTo(LocalDate(2025, 2, 22))

    }
}