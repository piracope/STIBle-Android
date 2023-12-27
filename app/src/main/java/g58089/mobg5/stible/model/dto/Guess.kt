package g58089.mobg5.stible.model.dto

import kotlinx.serialization.SerialName

/**
 * A Guess made by the user.
 */
data class Guess(
    /**
     * The name of the [Stop] the user thinks is the mystery stop.
     */
    @SerialName("input")
    val stopName: String,

    /**
     * How many tries are we at?
     */
    @SerialName("currNb")
    val tryNumber: Int,

    /**
     * Which mystery [Stop] this guess relates to.
     *
     * If this puzzleNumber is outdated, the guess
     * will be invalidated
     */
    @SerialName("lvlNumber")
    val puzzleNumber: Int,

    /**
     * The user's current language.
     *
     * Since the game is bilingual, the server has to
     * know which language this guess is in.
     */
    @SerialName("lang")
    val lang: String
)
