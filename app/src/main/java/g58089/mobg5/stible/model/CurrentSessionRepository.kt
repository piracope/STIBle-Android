package g58089.mobg5.stible.model

import g58089.mobg5.stible.model.database.GameRecap
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert and retrieval of [GameRecap] for statistics.
 */
interface CurrentSessionRepository {
    /**
     * Retrieves all game recaps from the provided data source.
     */
    fun getAllRecapsStream(): Flow<List<GameRecap>>

    /**
     * Inserts a new game recap in the data source
     */
    suspend fun insertRecap(recap: GameRecap)

    /**
     * Deletes all game recaps from the data source.
     */
    suspend fun clearGameHistory()
}