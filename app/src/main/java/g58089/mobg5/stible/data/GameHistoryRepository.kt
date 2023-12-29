package g58089.mobg5.stible.data

import g58089.mobg5.stible.data.dto.GameRecap
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert and retrieval of [GameRecap]s for statistics.
 */
interface GameHistoryRepository {
    /**
     * Retrieves all [GameRecap]s from the provided data source.
     */
    fun getAllRecapsStream(): Flow<List<GameRecap>>

    /**
     * Inserts a new [GameRecap] in the data source
     */
    suspend fun insertRecap(recap: GameRecap)

    /**
     * Deletes all [GameRecap]s from the data source.
     */
    suspend fun clearGameHistory()
}