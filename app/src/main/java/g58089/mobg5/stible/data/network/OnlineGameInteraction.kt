package g58089.mobg5.stible.data.network

import g58089.mobg5.stible.data.GameInteraction
import g58089.mobg5.stible.data.dto.Guess
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.dto.StopTranslation
import g58089.mobg5.stible.data.util.ErrorType
import g58089.mobg5.stible.data.util.Language
import g58089.mobg5.stible.data.util.STIBleException
import java.io.IOException

/**
 * [GameInteraction] that interacts with an online backend, provided by [STIBleApiService].
 */
class OnlineGameInteraction(private val stibleApi: STIBleApiService) : GameInteraction {
    override suspend fun guess(
        stopName: String,
        puzzleNumber: Int,
        tryNumber: Int,
        lang: Language
    ): GuessResponse {
        val guess = Guess(
            stopName = stopName,
            tryNumber = tryNumber,
            puzzleNumber = puzzleNumber,
            lang = lang.code
        )

        try {
            val response = stibleApi.guess(guess)

            // Guess was outdated
            if (response.code() == 205) {
                throw STIBleException(ErrorType.NEW_LEVEL_AVAILABLE)
            }

            // Guessed stop didn't exist
            if (!response.isSuccessful) {
                throw STIBleException(ErrorType.BAD_STOP)
            }


            val responseBody = response.body()
            if (responseBody == null) {
                // legit no idea how we could end up here
                throw STIBleException(ErrorType.UNKNOWN)
            } else {
                return responseBody
            }

        } catch (e: IOException) {
            throw STIBleException(ErrorType.NO_INTERNET)
        }
    }

    override suspend fun translateStop(
        stopName: String,
        oldLang: Language,
        newLang: Language
    ): String {
        val translation = StopTranslation(stopName, oldLang.code, newLang.code)
        try {
            return stibleApi.translate(translation)
        } catch (e: Exception) {
            throw STIBleException(ErrorType.TRANSLATION_FAILURE)
        }
    }
}