package g58089.mobg5.stible.data.database

import g58089.mobg5.stible.data.CurrentSessionRepository
import g58089.mobg5.stible.data.dto.GuessResponse
import kotlinx.coroutines.flow.Flow

/**
 * Retrieves and stores [GuessResponse]s in a local Room database for the day.
 */
class OfflineCurrentSessionRepository(private val sessionDao: CurrentSessionDao) :
    CurrentSessionRepository {
    override fun getAllGuessResponses(): Flow<List<GuessResponse>> = sessionDao.getAllItems()
    override suspend fun insertGuessResponse(guessResponse: GuessResponse) =
        sessionDao.insert(guessResponse)

    override suspend fun setStopName(guessResponse: GuessResponse, newStopName: String) {
        val newGuessResponse = guessResponse.copy(stopName = newStopName)
        sessionDao.update(newGuessResponse)
    }

    override suspend fun clearForNewSession() = sessionDao.wipeSession()
}