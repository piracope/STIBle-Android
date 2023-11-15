package g58089.mobg5.stible.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    @SerialName("access_token")
    val accessToken: String
)
