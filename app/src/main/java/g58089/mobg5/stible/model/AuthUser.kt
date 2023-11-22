package g58089.mobg5.stible.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data sent back by the login server once authenticated.
 */
@Serializable
data class AuthUser(
    /**
     * Token to authenticate all subsequent requests as a certain authenticated user.
     */
    @SerialName("access_token")
    val accessToken: String
)
