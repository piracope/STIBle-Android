package g58089.mobg5.stible.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A route is a particular path used by the STIB Network.
 *
 * For example, WIENER - GRAND-PLACE is Bus 95's route.
 * This differs from a Line where WIENER -> GRAND-PLACE is one Line
 * and GRAND-PLACE -> WIENER is another Line.
 *
 * You could say a Route is non-oriented, where a Line is.
 */
@Serializable
data class Route(
    /**
     * The Route's number.
     *
     * For example, the route WIENER - GRAND-PLACE has routeNumber 95.
     */
    @SerialName("route_short_name")
    val routeNumber: Int,

    /**
     * The Route logo background color.
     *
     * Contains the hexadecimal part, without the 0x.
     * For example, a white routeColor would have "FFFFFF" as value.
     */
    @SerialName("route_color")
    val routeColor: String,

    /**
     * The Route number color.
     *
     * Contains the hexadecimal part, without the 0x.
     * For example, a white routeNumberColor would have "FFFFFF" as value.
     */
    @SerialName("route_text_color")
    val routeNumberColor: String,
)
