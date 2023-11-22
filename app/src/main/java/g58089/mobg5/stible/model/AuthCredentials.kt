package g58089.mobg5.stible.model

import kotlinx.serialization.Serializable

/**
 * Data to send to the login server when authenticating.
 */
@Serializable
data class AuthCredentials(
    val email: String,
    val password: String
)
