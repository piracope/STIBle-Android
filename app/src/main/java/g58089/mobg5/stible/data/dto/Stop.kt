package g58089.mobg5.stible.data.dto

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
    val name: String = "CONGRES",

    /**
     * The Stop's latitude.
     */
    @SerialName("stop_lat")
    val latitude: Double = 50.850416,

    /**
     * The Stop's longitude.
     */
    @SerialName("stop_lon")
    val longitude: Double = 4.364237
)
