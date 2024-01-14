package g58089.mobg5.stible.data

import g58089.mobg5.stible.data.dto.GuessResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert and retrieval of [GuessResponse]s for today's session.
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
     * Updates an existing [GuessResponse.stopName] with a new name.
     */
    suspend fun setStopName(guessResponse: GuessResponse, newStopName: String)

    /**
     * Clears the current session to make way for a new one.
     */
    suspend fun clearForNewSession()
}