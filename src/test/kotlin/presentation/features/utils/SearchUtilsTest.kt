package presentation.features.utils

import com.google.common.truth.Truth.assertThat
import org.example.input_output.input.InputReader
import org.example.presentation.features.utils.SearchUtils
import org.junit.jupiter.api.Test
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull


class SearchUtilsTest {

    private lateinit var searchUtils: SearchUtils
    private lateinit var reader: InputReader

    @BeforeTest
    fun setup() {
        reader = mockk(relaxed = true)
        searchUtils = SearchUtils()
    }

    @ParameterizedTest
    @CsvSource(
        "null, false",
        "'', false",
        "' ', false",
        "'Y', true",
        "'y', true",
        "' Y ', true",
        "' y ', true",
        "'n', false",
        "'N', false",
        "'yes', false",
        "'no', false",
        "'  y  ', true",
        "'  Y  ', true",
        "'randomString', false",
        "'yYy', false",
        "'\t\n\r y \t\n\r', true"
    )
    fun `isValidSearchAgainInput should return expected result when input is varied`(input: String?, expected: Boolean) {
        // Given

        // When
        val actual = searchUtils.isValidSearchAgainInput(input)

        // Then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "'-1', 5, null",
        "'1.5', 5, null",
        "' 1 ', 5, 0",
        "' 5 ', 5, 4",
        "'0', 5, null",
        "'6', 5, null",
        "'1', 1, 0",
        "'randomString', 5, null"
    )
    fun `getValidMealIndex should handle edge cases`(input: String, size: Int, expectedStr: String?) {
        // Given
        val expected = if (expectedStr == "null") null else expectedStr?.toInt()
        every { reader.readStringOrNull() } returns input

        // When
        val actual = searchUtils.getValidMealIndex(reader, size)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `readTrimmedLowercaseInput should return null when reader returns null`() {
        // Given
        every { reader.readStringOrNull() } returns null

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `readTrimmedLowercaseInput should return empty string when input is empty`() {
        // Given
        every { reader.readStringOrNull() } returns ""

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `readTrimmedLowercaseInput should return empty string when input is only whitespace`() {
        // Given
        every { reader.readStringOrNull() } returns "   "

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `readTrimmedLowercaseInput should return lowercase string when input is mixed case with whitespace`() {
        // Given
        every { reader.readStringOrNull() } returns "  Hello "

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertThat(result).isEqualTo("hello")
    }

    @Test
    fun `readTrimmedLowercaseInput should return lowercase string when input is uppercase`() {
        // Given
        every { reader.readStringOrNull() } returns "WORLD"

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertThat(result).isEqualTo("world")
    }

    @Test
    fun `readTrimmedLowercaseInput should return trimmed lowercase character when input is single character with spaces`() {
        // Given
        every { reader.readStringOrNull() } returns "  y "

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertThat(result).isEqualTo("y")
    }

    @Test
    fun `readTrimmedLowercaseInput should return empty string when input is only spaces`() {
        // Given
        every { reader.readStringOrNull() } returns "   "

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertEquals("", result)
    }

    @Test
    fun `readTrimmedLowercaseInput should return lowercase of trimmed input`() {
        // Given
        every { reader.readStringOrNull() } returns "  HeLLo  "

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertEquals("hello", result)
    }

    @Test
    fun `readTrimmedLowercaseInput should return lowercase when already lowercase`() {
        // Given
        every { reader.readStringOrNull() } returns "  bye "

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertEquals("bye", result)
    }

    @Test
    fun `readTrimmedLowercaseInput should return null when input is null`() {
        // Given
        every { reader.readStringOrNull() } returns null

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertNull(result)
    }

    @Test
    fun `readTrimmedLowercaseInput should return empty string when input is only tabs and newlines`() {
        // Given
        every { reader.readStringOrNull() } returns "\t\n\r"

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertEquals("", result)
    }

    @Test
    fun `readTrimmedLowercaseInput should handle special characters correctly`() {
        // Given
        every { reader.readStringOrNull() } returns " \tHeLLo\n "

        // When
        val result = searchUtils.readTrimmedLowercaseInput(reader)

        // Then
        assertEquals("hello", result)
    }

    @ParameterizedTest
    @CsvSource(
        "null, 5, null",
        "'', 5, null",
        "'abc', 5, null",
        "'0', 5, null",
        "'6', 5, null",
        "'1', 5, 0",
        "'5', 5, 4",
        "'3', 5, 2",
        "'  1  ', 5, 0",
        "'-1', 5, null",
        "'1.5', 5, null",
        "'randomString', 5, null"
    )
    fun `getValidMealIndex should return expected index when input and size vary`(input: String, size: Int, expectedStr: String) {
        // Given
        val expected = if (expectedStr == "null") null else expectedStr.toInt()
        every { reader.readStringOrNull() } returns if (input == "null") null else input

        // When
        val actual = searchUtils.getValidMealIndex(reader, size)

        // Then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `readNonBlankTrimmedInput should return null when reader returns null`() {
        // Given
        every { reader.readStringOrNull() } returns null

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `readNonBlankTrimmedInput should return null when input is only whitespace`() {
        // Given
        every { reader.readStringOrNull() } returns "   "

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `readNonBlankTrimmedInput should return trimmed string when input is valid`() {
        // Given
        every { reader.readStringOrNull() } returns "  validInput  "

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assertThat(result).isEqualTo("validInput")
    }

    @Test
    fun `readNonBlankTrimmedInput should return null when input contains only special characters`() {
        // Given
        every { reader.readStringOrNull() } returns "\t\n\r"

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assertNull(result)
    }

    @Test
    fun `readNonBlankTrimmedInput should handle inputs with special characters and valid content`() {
        // Given
        every { reader.readStringOrNull() } returns " \t ValidInput \n "

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assertEquals("ValidInput", result)
    }

    @Test
    fun `readNonBlankTrimmedInput should return trimmed input when input has trailing special characters`() {
        // Given
        every { reader.readStringOrNull() } returns "ValidInput\t\n\r"

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assertEquals("ValidInput", result)
    }

    @Test
    fun `readNonBlankTrimmedInput should return input as-is when already trimmed and valid`() {
        // Given
        every { reader.readStringOrNull() } returns "cleanInput"

        // When
        val result = searchUtils.readNonBlankTrimmedInput(reader)

        // Then
        assertThat(result).isEqualTo("cleanInput")
    }

    @ParameterizedTest
    @CsvSource(
        "null, null",
        "'', null",
        "' ', null",
        "'Y', true",
        "'y', true",
        "' Y ', true",
        "' y ', true",
        "'n', null",
        "'N', null",
        "'yes', null",
        "'no', null",
        "'randomString', null",
        "'\t\n\r y \t\n\r', true"
    )
    fun `shouldSearchAgain should return expected boolean when input is varied`(input: String?, expectedStr: String?) {
        // Given
        val expected = if (expectedStr == "null") null else expectedStr?.toBoolean()
        every { reader.readStringOrNull() } returns input

        // When
        val actual = searchUtils.shouldSearchAgain(reader)

        // Then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "'\t Y \n', true",
        "'\t y \n', true",
        "'\t n \n', null",
        "'\t N \n', null",
        "'\t randomString \n', null"
    )
    fun `shouldSearchAgain should handle inputs with special characters`(input: String?, expectedStr: String?) {
        // Given
        val expected = if (expectedStr == "null") null else expectedStr?.toBoolean()
        every { reader.readStringOrNull() } returns input

        // When
        val actual = searchUtils.shouldSearchAgain(reader)

        // Then
        assertEquals(expected, actual)
    }
}
