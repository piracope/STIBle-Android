package g58089.mobg5.stible.model.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

/**
 * The response of the server to a user's guess.
 *
 * Provides valuable information about the mystery stop.
 */
@Entity(tableName = "current_session")
data class GuessResponse(

    @PrimaryKey(autoGenerate = true)
    @Transient
    val id: Int = 0,

    /**
     * The name of the stop guessed.
     */
    @SerialName("stop_name")
    @ColumnInfo("stop_name")
    val stopName: String,

    /**
     * The distance between the guessed stop and the mystery stop, in km.
     */
    @SerialName("distance")
    @ColumnInfo("distance")
    val distance: Double,

    /**
     * The "percentage" of success of this guess.
     *
     * "what even is a percentage of success in this context" I won't elaborate.
     */
    @SerialName("percentage")
    @ColumnInfo("percentage")
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
    @ColumnInfo("direction")
    val directionEmoji: String,

    /**
     * The name of the mystery stop.
     *
     * This will only be filled if this is in response
     * to a succeeding guess or if it was the user's last remaining try.
     *
     * Also it's a Stop for some reason.
     */
    @SerialName("secret")
    @Embedded("mystery")
    val mysteryStop: Stop? = null // FIXME: room doesn't know what to do with this
)
