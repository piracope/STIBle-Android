package g58089.mobg5.stible.model.database

import g58089.mobg5.stible.model.CurrentSessionRepository
import g58089.mobg5.stible.model.dto.GuessResponse
import kotlinx.coroutines.flow.Flow

/**
 * Retrieves and stores [GuessResponse]s in a local database for the day.
 */
class OfflineCurrentSessionRepository(private val sessionDao: CurrentSessionDao) :
    CurrentSessionRepository {
    override fun getAllGuessResponses(): Flow<List<GuessResponse>> = sessionDao.getAllItems()
    override suspend fun insertGuessResponse(guessResponse: GuessResponse) =
        sessionDao.insert(guessResponse)

    override suspend fun clearForNewSession() = sessionDao.wipeSession()
}