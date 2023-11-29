package g58089.mobg5.stible.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * All data essential to the working of the game.
 */
@Serializable
data class GameRules(

    /**
     * All routes passing on the mystery Stop.
     */
    @SerialName("routes")
    val puzzleRoutes: List<Route> = listOf(),

    /**
     * The list of all possible stops that can act as a guess.
     *
     * All valid guesses.
     */
    @SerialName("stops")
    val stops: List<String> = listOf(),

    /**
     * The maximum amount of time the user can guess in a day.
     */
    @SerialName("max")
    val maxGuessCount: Int = 6,

    /**
     * The puzzle number for which this data is valid.
     *
     * The puzzle number is incremented daily (usually) server-side.
     * All guesses where the puzzleNumber differs from the server's
     * will be discarded.
     */
    @SerialName("lvlNumber")
    val puzzleNumber: Int = -1
)
