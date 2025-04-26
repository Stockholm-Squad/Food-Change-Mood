package presentation.features.utils

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.input_output.input.InputReader
import org.example.input_output.output.OutputPrinter
import org.example.presentation.features.utils.SearchUtils
import org.example.utils.Constants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SearchUtilsTest {

    private lateinit var reader: InputReader
    private lateinit var printer: OutputPrinter
    private lateinit var searchUtils: SearchUtils

    @BeforeEach
    fun setup() {
        reader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        searchUtils = SearchUtils(printer)
    }

    @Test
    fun `readNonBlankTrimmedInput should return null for blank input`() {
        every { reader.readStringOrNull() } returns "   "
        val result = searchUtils.readNonBlankTrimmedInput(reader)
        assertThat(result).isNull()
    }

    @Test
    fun `readNonBlankTrimmedInput should return trimmed lowercase input`() {
        every { reader.readStringOrNull() } returns "  TeStInG  "
        val result = searchUtils.readNonBlankTrimmedInput(reader)
        assertThat(result).isEqualTo("testing")
    }

    @Test
    fun `readNonBlankTrimmedInput should return null when input is only tabs and newlines`() {
        every { reader.readStringOrNull() } returns "\t\n\r"
        val result = searchUtils.readNonBlankTrimmedInput(reader)
        assertThat(result).isNull()
    }

    @ParameterizedTest
    @CsvSource(
        "n, true",
        "N, true",
        "y, false",
        "invalid, false"
    )
    fun `isSkipInput should correctly identify skip input`(input: String, expected: Boolean) {
        val result = searchUtils.isSkipInput(input)
        assertThat(result).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "y, true",
        "Y, true",
    )
    fun `shouldSearchAgain should correctly handle user input`(input: String, expected: String?) {
        every { reader.readStringOrNull() } returns input

        val result = searchUtils.shouldSearchAgain(reader)

        if (expected == null) {
            assertThat(result).isNull()
        } else {
            assertThat(result).isEqualTo(expected.toBoolean())
        }
    }

    @Test
    fun `getValidMealIndex should return null for skip input`() {
        every { reader.readStringOrNull() } returns "n"
        val result = searchUtils.getValidMealIndex(reader, 5)
        assertThat(result).isNull()
    }

    @Test
    fun `getValidMealIndex should return valid index for valid input`() {
        every { reader.readStringOrNull() } returns "3"
        val result = searchUtils.getValidMealIndex(reader, 5)
        assertThat(result).isEqualTo(2)
    }

    @Test
    fun `getValidMealIndex should handle invalid input and prompt again`() {
        every { reader.readStringOrNull() } returnsMany listOf("invalid", "n")
        val result = searchUtils.getValidMealIndex(reader, 5)
        assertThat(result).isNull()
        verify { printer.printLine(Constants.INVALID_SELECTION_MESSAGE) }
    }
}
