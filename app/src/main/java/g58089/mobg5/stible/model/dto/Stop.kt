package g58089.mobg5.stible.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Stop in the STIB network.
 */
@Serializable
data class Stop(
    /**
     * The Stop's name.
     */
    @SerialName("stop_name")
    val stopName: String,

    // TODO: get geo-coordinates too.
)
