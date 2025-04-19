import data.MealCsvParser
import data.MealCsvReader
import org.example.data.CsvLineFormatter
import org.example.data.MealCsvRepository
import org.example.logic.MealsRepository
import org.koin.dsl.module

val dataModule = module {
    single { CsvLineFormatter() }
    single { CsvLineHandler() }

    single { MealCsvParser(get()) }
    single { MealCsvReader(get(), get()) }

    single<MealsRepository> { MealCsvRepository(get(), get()) }
}