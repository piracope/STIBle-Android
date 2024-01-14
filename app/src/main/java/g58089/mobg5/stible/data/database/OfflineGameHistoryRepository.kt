package g58089.mobg5.stible.data.database

import g58089.mobg5.stible.data.GameHistoryRepository
import g58089.mobg5.stible.data.dto.GameRecap

/**
 * Retrieves and stores [GameRecap]s in a local database.
 */
class OfflineGameHistoryRepository(private val historyDao: GameHistoryDao) : GameHistoryRepository {
    override fun getAllRecapsStream() = historyDao.getAllItems()
    override suspend fun clearGameHistory() = historyDao.clearHistory()
    override suspend fun insertRecap(recap: GameRecap) = historyDao.insert(recap)
}