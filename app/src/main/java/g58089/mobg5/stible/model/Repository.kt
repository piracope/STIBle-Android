package g58089.mobg5.stible.model

import g58089.mobg5.stible.model.dto.GameRules
import g58089.mobg5.stible.model.dto.Guess
import g58089.mobg5.stible.model.dto.GuessResponse
import g58089.mobg5.stible.model.util.Language
import g58089.mobg5.stible.network.STIBleApi
import retrofit2.Response

/**
 * Access point for HTTP requests and database access.
 */
object Repository {

    /**
     * The Retrofit service.
     *
     * (so i don't have to do .retrofitService each time.
     */
    private val stibleApi = STIBleApi.retrofitService

    /**
     * Get the initial data, according to the user's language.
     */
    suspend fun getGameRules(lang: Language): GameRules {
        return stibleApi.start(lang.code)
    }

    suspend fun guess(stopName: String, puzzleNumber: Int, tryNumber: Int, lang: Language): Response<GuessResponse> {
        val guess = Guess(
            stopName = stopName,
            tryNumber = tryNumber,
            puzzleNumber = puzzleNumber,
            lang = lang.code
        )

        return stibleApi.guess(guess)
    }
}