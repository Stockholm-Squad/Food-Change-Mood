package utils

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
        val line = "Name,Age,City"
        val result = csvLineHandler.handleLine(line)
        assertThat(result).isEqualTo(line)
    }

    @Test
    fun `should return null when line has unclosed quotes`() {
        val line = "\"Name,Age"
        val result = csvLineHandler.handleLine(line)
        assertThat(result).isNull()
    }

    @Test
    fun `should combine multiple lines with newlines when quotes span multiple lines`() {
        val line1 = "\"Multi"
        val line2 = "line"
        val line3 = "value\""

        assertThat(csvLineHandler.handleLine(line1)).isNull()
        assertThat(csvLineHandler.handleLine(line2)).isNull()
        assertThat(csvLineHandler.handleLine(line3)).isEqualTo("\"Multi\nline\nvalue\"")
    }

    @Test
    fun `should return complete line when quoted field ends in same line`() {
        val line = "\"Name,Age,City\""
        val result = csvLineHandler.handleLine(line)
        assertThat(result).isEqualTo(line)
    }

    @Test
    fun `should return empty lines correctly when line is empty`() {
        assertThat(csvLineHandler.handleLine("")).isEqualTo("")
    }


    @Test
    fun `should handle consecutive quote characters correctly when number of quotes is even`() {
        val line = "\"\"\"\"\"\"" // """"
        val result = csvLineHandler.handleLine(line)
        assertThat(result).isEqualTo(line)
    }


}