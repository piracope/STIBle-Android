package g58089.mobg5.stible.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class containing all information required by the backend to translate a stop's name.
 */
@Serializable
data class StopTranslation(
    /**
     * The name of the Stop to translate.
     */
    @SerialName("stop_name") val stopName: String,

    /**
     * The current language of [stopName]
     */
    @SerialName("oldLang") val oldLang: String,

    /**
     * The language [stopName] should be translated to.
     */
    @SerialName("newLang") val newLang: String
)
