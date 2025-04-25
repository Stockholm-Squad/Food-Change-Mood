package data.utils

import com.google.common.truth.Truth.assertThat
import org.example.data.utils.CsvLineHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CsvLineHandlerTest {
    private lateinit var csvLineHandler: CsvLineHandler

    @BeforeEach
    fun setUp() {
        csvLineHandler = CsvLineHandler()
    }

    @Test
    fun `should return complete line immediately when line has no quotes`() {
        // Given
        val line = "Name,Age,City"

        // When
        val result = csvLineHandler.handleLine(line)

        // Then
        assertThat(result).isEqualTo(line)
    }

    @Test
    fun `should return null when line has unclosed quotes`() {
        // Given
        val line = "\"Name,Age"

        // When
        val result = csvLineHandler.handleLine(line)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should combine multiple lines with newlines when quotes span multiple lines`() {
        // Given
        val line1 = "\"Multi"
        val line2 = "line"
        val line3 = "value\""

        // When
        val result1 = csvLineHandler.handleLine(line1)
        val result2 = csvLineHandler.handleLine(line2)
        val result3 = csvLineHandler.handleLine(line3)

        // Then
        assertThat(result1).isNull()
        assertThat(result2).isNull()
        assertThat(result3).isEqualTo("\"Multi\nline\nvalue\"")
    }

    @Test
    fun `should return complete line when quoted field ends in same line`() {
        // Given
        val line = "\"Name,Age,City\""

        // When
        val result = csvLineHandler.handleLine(line)

        // Then
        assertThat(result).isEqualTo(line)
    }

    @Test
    fun `should return empty string when line is empty`() {
        // Given
        val line = ""

        // When
        val result = csvLineHandler.handleLine(line)

        // Then
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `should handle consecutive quote characters correctly when number of quotes is even`() {
        // Given
        val line = "\"\"\"\"\"\"" // """"

        // When
        val result = csvLineHandler.handleLine(line)

        // Then
        assertThat(result).isEqualTo(line)
    }

    @Test
    fun `should handle mixed quoted and unquoted content across multiple lines`() {
        // Given
        val line1 = "Text,\"Quoted"
        val line2 = "Value\",More"

        // When
        val result1 = csvLineHandler.handleLine(line1)
        val result2 = csvLineHandler.handleLine(line2)

        // Then
        assertThat(result1).isNull()
        assertThat(result2).isEqualTo("Text,\"Quoted\nValue\",More")
    }

    @Test
    fun `should handle multiple independent quoted sections`() {
        // Given
        val line1 = "Header,\"Value1"
        val line2 = "End1\""
        val line3 = "\"Value2"
        val line4 = "End2\""

        // When
        val result1 = csvLineHandler.handleLine(line1)
        val result2 = csvLineHandler.handleLine(line2)
        val result3 = csvLineHandler.handleLine(line3)
        val result4 = csvLineHandler.handleLine(line4)

        // Then
        assertThat(result1).isNull()
        assertThat(result2).isEqualTo("Header,\"Value1\nEnd1\"")
        assertThat(result3).isNull()
        assertThat(result4).isEqualTo("\"Value2\nEnd2\"")
    }

    @Test
    fun `should handle newlines within quoted content`() {
        // Given
        val line1 = "\"Line1"
        val line2 = "Line2"
        val line3 = "Line3\""

        // When
        val result1 = csvLineHandler.handleLine(line1)
        val result2 = csvLineHandler.handleLine(line2)
        val result3 = csvLineHandler.handleLine(line3)

        // Then
        assertThat(result1).isNull()
        assertThat(result2).isNull()
        assertThat(result3).isEqualTo("\"Line1\nLine2\nLine3\"")
    }

    @Test
    fun `should handle transition from unquoted to quoted content`() {
        // Given
        val line1 = "Start,"
        val line2 = "\"Middle"
        val line3 = "End\""

        // When
        val result1 = csvLineHandler.handleLine(line1)
        val result2 = csvLineHandler.handleLine(line2)
        val result3 = csvLineHandler.handleLine(line3)

        // Then
        assertThat(result1).isEqualTo("Start,")
        assertThat(result2).isNull()
        assertThat(result3).isEqualTo("\"Middle\nEnd\"")
    }
}