package g58089.mobg5.stible.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import g58089.mobg5.stible.model.dto.GameRecap
import kotlinx.coroutines.flow.Flow

/**
 * DAO for the games history.
 *
 * The games history is the record of all [GameRecap]s.
 * It can be used by a Statistics screen to show metrics about the user's performance.
 */
@Dao
interface GameHistoryDao {
    /**
     * Inserts a new [GameRecap] to the games history.
     */
    @Insert
    suspend fun insert(guess: GameRecap)

    /**
     * Clears the games history.
     */
    @Query("DELETE FROM history")
    suspend fun clearHistory()

    /**
     * Get all [GameRecap]s.
     */
    @Query("SELECT * FROM history")
    fun getAllItems(): Flow<List<GameRecap>>
}