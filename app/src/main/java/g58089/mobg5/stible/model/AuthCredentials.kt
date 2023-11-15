package g58089.mobg5.stible.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthCredentials(
    val email: String,
    val password: String
)
