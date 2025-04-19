package presentation;

import org.example.logic.usecases.GymHelperUseCase
import org.example.utils.Constants


class GymHelperUI(
    private val gymHelperUseCase: GymHelperUseCase?
) {

    fun useGymHelper() {
        print("🔥 Enter desired calories: ")
        val desiredCalories = readlnOrNull()?.toFloatOrNull()
        if (desiredCalories == null) {
            println(Constants.INVALID_INPUT)
            return
        }

        print("🔥 Enter desired proteins: ")
        val desiredProteins = readlnOrNull()?.toFloatOrNull()

        if (desiredProteins == null) {
            println(Constants.INVALID_INPUT)
            return
        }

        try {
            gymHelperUseCase?.getGymHelperMeals(
                desiredCalories = desiredCalories,
                desiredProteins = desiredProteins
            )?.takeIf {
                it.isNotEmpty()
            }?.forEach {
                println(it)
            } ?: println(Constants.NO_MEALS_FOR_GYM_HELPER)

        } catch (throwable: Throwable) {
            println(throwable.message)
        }

    }

}
