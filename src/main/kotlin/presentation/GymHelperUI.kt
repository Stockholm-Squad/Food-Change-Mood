package presentation;

import org.example.logic.usecases.GymHelperUseCase
import org.example.utils.Messages

class GymHelperUI(
    private val gymHelperUseCase: GymHelperUseCase?
) {

     fun useGymHelper() {
         print("🔥 Enter desired calories: ")
         val desiredCalories = readlnOrNull()?.toFloatOrNull()
         if (desiredCalories == null) {
             println(Messages.INVALID_INPUT.messages)
             return
         }

         print("🔥 Enter desired proteins: ")
         val desiredProteins = readlnOrNull()?.toFloatOrNull()

         if (desiredProteins == null) {
             println(Messages.INVALID_INPUT.messages)
             return
         }

         try {
             gymHelperUseCase?.getGymHelperMeals(
                 desiredCalories = desiredCalories,
                 desiredProteins = desiredProteins
             )?.takeIf {
                 it.isNotEmpty()
             }?.forEach{
                 println(it)
             } ?: println(Messages.NO_MEALS_FOR_GYM_HELPER)

         } catch (throwable: Throwable) {
             println(throwable.message)
         }

    }

}
