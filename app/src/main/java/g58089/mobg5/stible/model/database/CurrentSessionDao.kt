package g58089.mobg5.stible.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import g58089.mobg5.stible.model.dto.GuessResponse

@Dao
interface CurrentSessionDao {
    @Insert
    suspend fun insert(guess: GuessResponse)

    @Query("SELECT * FROM current_session")
    suspend fun getAllItems(): List<GuessResponse> // TODO: figure out if this should be a Flow
}