package g58089.mobg5.stible.model

import kotlinx.serialization.SerialName

/**
 * A route is a particular path used by the STIB Network.
 *
 * For example, WIENER - GRAND-PLACE is Bus 95's route.
 * This differs from a Line where WIENER -> GRAND-PLACE is one Line
 * and GRAND-PLACE -> WIENER is another Line.
 *
 * You could say a Route is non-oriented, where a Line is.
 */
data class Route(
    /**
     * The Route's number.
     *
     * It is a String to accommodate for potential
     * updates where T-Buses (called T92 for example) will
     * be taken into account.
     *
     * Also because we just use it to display so no need to have an integer.
     */
    @SerialName("route_short_name")
    val routeNumber: String,

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
