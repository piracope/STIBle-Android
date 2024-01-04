package g58089.mobg5.stible.data.network

import android.util.Log
import g58089.mobg5.stible.data.GameInteraction
import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.dto.Guess
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.dto.StopTranslation
import g58089.mobg5.stible.data.util.Language
import retrofit2.Response

/**
 * Access point for HTTP requests and database access.
 */
class OnlineGameInteraction(private val stibleApi: STIBleApiService) : GameInteraction {
    override suspend fun getGameRules(lang: Language): GameRules {
        return stibleApi.start(lang.code)
    }

    override suspend fun guess(
        stopName: String,
        puzzleNumber: Int,
        tryNumber: Int,
        lang: Language
    ): Response<GuessResponse> {
        val guess = Guess(
            stopName = stopName,
            tryNumber = tryNumber,
            puzzleNumber = puzzleNumber,
            lang = lang.code
        )

        return stibleApi.guess(guess)
    }

    override suspend fun translateStop(
        stopName: String,
        oldLang: Language,
        newLang: Language
    ): String {
        val translation = StopTranslation(stopName, oldLang.code, newLang.code)
        Log.d("OnlineGameInteraction", "sending this translation: $translation")
        return stibleApi.translate(translation)
    }
}