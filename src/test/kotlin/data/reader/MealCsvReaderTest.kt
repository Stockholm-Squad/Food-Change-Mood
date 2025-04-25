package data.reader

import io.mockk.every
import io.mockk.mockk
import org.example.data.reader.MealCsvReader
import org.example.data.reader.MealReader
import org.example.data.utils.CsvLineHandler
import org.junit.jupiter.api.BeforeEach
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MealCsvReaderTest {

    private lateinit var csvFile: File
    private lateinit var csvLineHandler: CsvLineHandler
    private lateinit var mealCsvReader: MealReader
    private lateinit var reader: BufferedReader

    @BeforeEach
    fun setUp() {
        csvFile = mockk()
        csvLineHandler = mockk()
        mealCsvReader = MealCsvReader(csvFile, csvLineHandler)
        reader = mockk(relaxed = true)
        every { csvFile.bufferedReader() } returns reader
    }

    @Test
    fun `readLinesFromFile () should return success when file has valid lines`() {
        // Given
        val lines = listOf("Header", "Line1", "Line2")
        val processedLines = listOf("Processed1", "Processed2")
        every { reader.readLine() } returns lines[0]
        every { reader.forEachLine(any()) } answers {
            val block = arg<(String) -> Unit>(0)
            block("Line1")
            block("Line2")
        }

        every { csvLineHandler.handleLine("Line1") } returns "Processed1"
        every { csvLineHandler.handleLine("Line2") } returns "Processed2"

        // When
        val result = mealCsvReader.readLinesFromFile()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(processedLines, result.getOrNull())
    }

    @Test
    fun `readLinesFromFile () should return failure when exception occurs while reading`() {
        // Given
        every { csvFile.bufferedReader() } throws IOException("File read error")

        // When
        val result = mealCsvReader.readLinesFromFile()

        // Then
        assertTrue(result.isFailure)
        assertEquals("File read error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `readLinesFromFile () should skip invalid lines that return null from handler`() {
        // Given
        val lines = listOf("Header", "InvalidLine", "ValidLine1", "InvalidLine2", "ValidLine2")
        val processedLines = listOf("ProcessedValidLine1", "ProcessedValidLine2")

        every { reader.readLine() } returns lines[0]
        every { reader.forEachLine(any()) } answers {
            val block = arg<(String) -> Unit>(0)
            lines.forEach { line -> block(line) }
        }
        every { csvLineHandler.handleLine("ValidLine1") } returns "ProcessedValidLine1"
        every { csvLineHandler.handleLine("ValidLine2") } returns "ProcessedValidLine2"

        // When
        val result = mealCsvReader.readLinesFromFile()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(processedLines, result.getOrNull())
    }

    @Test
    fun `readLinesFromFile () should return empty list when all lines are invalid`() {
        // Given
        val lines = listOf<String?>(null, null, null)
        val processedLines = emptyList<String>()

        every { reader.readLine() } returns lines[0]
        every { reader.forEachLine(any()) } answers {
            val block = arg<(String?) -> Unit>(0)
            lines.forEach { line -> block(line) }
        }
        every { csvLineHandler.handleLine(any()) } returns null

        // When
        val result = mealCsvReader.readLinesFromFile()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(processedLines, result.getOrNull())
    }

    @Test
    fun `readLinesFromFile () should ignore null lines and process valid ones when mixed`() {
        // Given
        val lines = listOf("Line1", null, "Line2", null, "Line3")
        val processedLine = listOf("processedLine1", "processedLine2", "processedLine3")

        every { reader.readLine() } returns lines[0]
        every { reader.forEachLine(any()) } answers {
            val block = arg<(String?) -> Unit>(0)
            lines.forEach { line -> block(line) }
        }
        every { csvLineHandler.handleLine("Line1") } returns "processedLine1"
        every { csvLineHandler.handleLine("Line2") } returns "processedLine2"
        every { csvLineHandler.handleLine("Line3") } returns "processedLine3"

        // When
        val result = mealCsvReader.readLinesFromFile()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(processedLine, result.getOrNull())
    }

    @Test
    fun `readLinesFromFile () should return failure if exception occurs while reading first line`() {
        // Given
        every { reader.readLine() } throws IOException("File read error")

        // When
        val result = mealCsvReader.readLinesFromFile()

        // Then
        assertTrue(result.isFailure)
        assertEquals("File read error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `readLinesFromFile () should return empty list when file is empty`() {
        // Given
        val lines = emptyList<String>()
        every { reader.readLine() } returns null

        // When
        val result = mealCsvReader.readLinesFromFile()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(lines, result.getOrNull())
    }
}
