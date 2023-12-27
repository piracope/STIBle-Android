package g58089.mobg5.stible.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import g58089.mobg5.stible.model.dto.GuessResponse
import kotlinx.coroutines.flow.Flow

/**
 * DAO for the current play session history.
 *
 * The current session is all [GuessResponse] received related to a certain day.
 * If today's puzzle number is greater than the stored puzzle number, the current session
 * should be wiped and start anew.
 */
@Dao
interface CurrentSessionDao {
    /**
     * Adds a new [GuessResponse] to today's play session.
     */
    @Insert
    suspend fun insert(guess: GuessResponse)

    /**
     * Retrieves all [GuessResponse] made during the day.
     */
    @Query("SELECT * FROM current_session")
    fun getAllItems(): Flow<List<GuessResponse>>

    /**
     * Clears today's session.
     */
    @Query("DELETE FROM current_session")
    suspend fun wipeSession()
}