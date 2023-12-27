package g58089.mobg5.stible.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * The information about a finished game session.
 */
@Entity(tableName = "history")
data class GameRecap(
    /**
     * The server-provided id for this puzzle.
     */
    @PrimaryKey
    val puzzleNumber: Int,

    /**
     * The number of guesses it took for the user to guess the mystery stop.
     */
    val guessCount: Int,

    /**
     * The closest the user has been to find the mystery stop.
     */
    val bestPercentage: Double,

    /**
     * The name of the mystery stop the user tried to guess.
     */
    val mysteryStop: String

    // TODO: add coordinates here
)
