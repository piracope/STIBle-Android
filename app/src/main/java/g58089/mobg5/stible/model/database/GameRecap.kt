package g58089.mobg5.stible.model.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import g58089.mobg5.stible.model.dto.Stop


/**
 * The information about a finished game session.
 */
@Entity(tableName = "history")
data class GameRecap(
    /**
     * The server-provided id for this puzzle.
     */
    @PrimaryKey
    @ColumnInfo("puzzle_number")
    val puzzleNumber: Int,

    /**
     * The number of guesses it took for the user to guess the mystery stop.
     */
    @ColumnInfo("guess_count")
    val guessCount: Int,

    /**
     * The closest the user has been to find the mystery stop.
     */
    @ColumnInfo("best_percentage")
    val bestPercentage: Double,

    /**
     * The name of the mystery [Stop] the user tried to guess.
     */
    @Embedded("mystery")
    val mysteryStop: Stop
)
