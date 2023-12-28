package g58089.mobg5.stible.model

import g58089.mobg5.stible.model.dto.GuessResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert and retrieval of [GuessResponse] for today's session.
 */
interface CurrentSessionRepository {
    /**
     * Retrieves all [GuessResponse]s of today from the provided data source.
     */
    fun getAllGuessResponses(): Flow<List<GuessResponse>>

    /**
     * Inserts a new [GuessResponse] in the data source
     */
    suspend fun insertGuessResponse(guessResponse: GuessResponse)

    /**
     * Clears the current session to make way for a new one.
     */
    suspend fun clearForNewSession()
}