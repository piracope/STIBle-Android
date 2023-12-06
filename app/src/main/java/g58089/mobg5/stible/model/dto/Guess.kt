package g58089.mobg5.stible.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Guess made by the user.
 */
@Serializable
data class Guess(
    /**
     * The Stop the user is giving as a guess.
     */
    @SerialName("input")
    val stopName: String,

    /**
     * How many tries are we at?
     */
    @SerialName("currNb")
    val tryNumber: Int,

    /**
     * Which mystery stop this guess relates to.
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
