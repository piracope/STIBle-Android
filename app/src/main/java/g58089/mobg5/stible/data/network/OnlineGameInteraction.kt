package g58089.mobg5.stible.data.network

import g58089.mobg5.stible.data.GameInteraction
import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.dto.Guess
import g58089.mobg5.stible.data.dto.GuessResponse
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
}