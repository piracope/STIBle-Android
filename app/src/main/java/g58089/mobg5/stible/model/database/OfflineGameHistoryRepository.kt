package g58089.mobg5.stible.model.database

import g58089.mobg5.stible.model.GameHistoryRepository

/**
 * Retrieves and stores [GameRecap]s in a local database.
 *
 * NOTE: I'm following the Google codelab, but that may become useful if for whatever reason
 * I want to store the recaps in a cloud database or something.
 */
class OfflineGameHistoryRepository(private val historyDao: GameHistoryDao) : GameHistoryRepository {
    override fun getAllRecapsStream() = historyDao.getAllItems()
    override suspend fun clearGameHistory() = historyDao.clearHistory()
    override suspend fun insertRecap(recap: GameRecap) = historyDao.insert(recap)

}