package g58089.mobg5.stible.model.dto

import androidx.room.ColumnInfo
import kotlinx.serialization.SerialName

/**
 * A Stop in the STIB network.
 */
data class Stop(
    /**
     * The Stop's name.
     */
    @SerialName("stop_name")
    @ColumnInfo("stop_name")
    val stopName: String,

    // TODO: get geo-coordinates too.
)
