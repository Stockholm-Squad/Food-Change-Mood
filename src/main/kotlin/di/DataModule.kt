import org.example.data.parser.MealCsvParser
import org.example.data.reader.MealCsvReader
import org.example.data.dataSource.MealCsvDataSource
import org.example.data.dataSource.MealDataSource
import org.example.data.parser.MealParser
import org.example.data.reader.MealReader
import org.example.data.utils.CsvLineFormatter
import org.example.data.repository.MealCsvRepository
import org.example.data.utils.CsvLineHandler
import org.example.logic.repository.MealsRepository
import org.koin.dsl.module

val dataModule = module {
    single { CsvLineFormatter() }
    single { CsvLineHandler() }

    single<MealParser> { MealCsvParser(get(), get()) }
    single<MealReader> { MealCsvReader(get(), get()) }
    single<MealDataSource> { MealCsvDataSource(get(), get()) }

    single<MealsRepository> { MealCsvRepository(get()) }
}