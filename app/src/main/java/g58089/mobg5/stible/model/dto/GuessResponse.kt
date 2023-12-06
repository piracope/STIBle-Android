package g58089.mobg5.stible.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The response of the server to a user's guess.
 *
 * Provides valuable information about the mystery stop.
 */
@Serializable
data class GuessResponse(
    /**
     * The name of the stop guessed.
     */
    @SerialName("stop_name")
    val stopName: String,

    /**
     * The distance between the guessed stop and the mystery stop, in km.
     */
    @SerialName("distance")
    val distance: Double,

    /**
     * The "percentage" of success of this guess.
     *
     * "what even is a percentage of success in this context" I won't elaborate.
     */
    @SerialName("percentage")
    val percentage: Double,

    /**
     * An Emoji showing the direction of the mystery stop from the guessed stop.
     *
     * Or '✅' if the guess succeeded
     *
     * ...I curse my past self for sending this as an emoji instead of just
     * passing a value and then interpreting that value in the view.
     */
    @SerialName("direction")
    val directionEmoji : String,

    /**
     * The name of the mystery stop.
     *
     * This will only be filled if this is in response
     * to a succeeding guess or if it was the user's last remaining try.
     *
     * Also it's a Stop for some reason.
     */
    @SerialName("secret")
    val mysteryStop: Stop? = null
)
