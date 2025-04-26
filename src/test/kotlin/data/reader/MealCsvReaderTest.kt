package data.reader

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import junit.framework.TestCase.assertTrue
import org.example.data.reader.MealCsvReader
import org.example.data.utils.CsvLineHandler
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.BufferedReader
import java.io.File
import kotlin.test.Test

class MealCsvReaderTest {
    private lateinit var csvFile: File
    private lateinit var csvLineHandler: CsvLineHandler
    private lateinit var reader: MealCsvReader
    private lateinit var bufferedReader: BufferedReader
    @BeforeEach
    fun setUp() {
        csvFile = File.createTempFile("food",".csv")
        csvLineHandler = mockk()
        reader = MealCsvReader(csvFile, csvLineHandler)
        mockkConstructor(BufferedReader::class)
        bufferedReader= mockk<BufferedReader>(relaxed = true)

    }

    @AfterEach
    fun tearDown() {
        csvFile.delete()
    }

    @Test
    fun `readLinesFromFile () should read and process all valid lines except header`() {
        //Given
        csvFile.writeText(
            """
                name,calories
                Pizza,300
                Salad,150
            """.trimIndent()
        )

        every { csvLineHandler.handleLine("Pizza,300") } returns "Pizza:300"
        every { csvLineHandler.handleLine("Salad,150") } returns "Salad:150"

        //When
        val result = reader.readLinesFromFile()

        //Then
        assertThat(result.getOrNull()).isEqualTo(listOf("Pizza:300", "Salad:150"))
    }

    @Test
    fun `readLinesFromFile () should skip invalid lines where handleLine returns null`() {
        //Given
        csvFile.writeText(
            """
                name,calories
                InvalidLine
                GoodLine,123
            """.trimIndent()
        )

        every { csvLineHandler.handleLine("InvalidLine") } returns null
        every { csvLineHandler.handleLine("GoodLine,123") } returns "Good:123"

        //When
        val result = reader.readLinesFromFile()

        //Then
        assertThat(result.getOrNull()).isEqualTo(listOf("Good:123"))
    }

    @Test
    fun `readLinesFromFile () should return empty list if only header is present`() {
        // Given
        csvFile.writeText("name,calories\n")

        // When
        val result = reader.readLinesFromFile()

        // Then
        val lines = result.getOrThrow()
        assertTrue(lines.isEmpty())
    }


    @Test
    fun `readLinesFromFile() should skip invalid lines where handleLine returns null`() {
        // Given
        csvFile.writeText(
            """
            name,calories
            InvalidLine
            GoodLine,123
        """.trimIndent()
        )
        every { csvLineHandler.handleLine("InvalidLine") } returns null
        every { csvLineHandler.handleLine("GoodLine,123") } returns "Good:123"


        // When
        val result = reader.readLinesFromFile()

        // Then
        assertThat(result.getOrNull()).isEqualTo(listOf("Good:123"))
    }

    @Test
    fun `readLinesFromFile() should throw ReadFailedException when an unexpected error happens during file reading`() {
        // Given
        val fakeBufferedReader = mockk<BufferedReader>()
        every { fakeBufferedReader.readLine() } throws RuntimeException("Unexpected error")
        every { fakeBufferedReader.close() } returns Unit

        val reader = MealCsvReader(
            File("any.csv"),
            csvLineHandler = mockk(relaxed = true)
        )
        // When & Then
         assertThrows<java.io.FileNotFoundException> {
            reader.readLinesFromFile().getOrThrow()
        }

    }

}




