package g58089.mobg5.stible.model.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * The response of the server to a user's [Guess].
 *
 * Provides valuable information about the mystery [Stop].
 */
@Entity(tableName = "current_session")
@Serializable
data class GuessResponse(

    @PrimaryKey(autoGenerate = true)
    @Transient
    val id: Int = 0,

    /**
     * The name of the [Stop] guessed.
     */
    @SerialName("stop_name")
    @ColumnInfo("stop_name")
    val stopName: String,

    /**
     * The distance between the guessed [Stop] and the mystery stop, in km.
     */
    @SerialName("distance")
    @ColumnInfo("distance")
    val distance: Double,

    /**
     * The "percentage" of success of this [Guess].
     *
     * The biggest distance between two stops is 23 km.
     * If a guess is 2.3km from the mystery stop, it is 90% close.
     */
    @SerialName("percentage")
    @ColumnInfo("percentage")
    val proximityPecentage: Double,

    /**
     * An Emoji showing the direction of the mystery [Stop] from the guessed stop.
     *
     * Or '✅' if the guessed stop is the mystery stop.
     *
     * ...I curse my past self for sending this as an emoji instead of just
     * passing a value and then interpreting that value in the view.
     */
    @SerialName("direction")
    @ColumnInfo("direction")
    val directionEmoji: String,

    /**
     * The mystery [Stop].
     *
     * This field will only be non-null if the server deems that our game session is over.
     * This occurs if :
     *  - the guessed stop is the mystery stop -> the user won
     *  - the user ran out of possible tries -> the user lost
     */
    @SerialName("secret")
    @Embedded("mystery")
    val mysteryStop: Stop? = null // FIXME: room doesn't know what to do with this
)
