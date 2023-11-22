package g58089.mobg5.stible.ui

import g58089.mobg5.stible.model.ErrorType
import g58089.mobg5.stible.network.AuthUser

/**
 * Defines all possible states of the login process.
 */
sealed interface LoginState {
    /**
     * The user is authenticated.
     *
     * Accompany this object with information about the authenticated user.
     */
    data class Success(val authUser: AuthUser) : LoginState

    /**
     * The login failed.
     *
     * Accompany this object with the reason of the error.
     */
    data class Error(val error: ErrorType) : LoginState

    /**
     * Nothing is happening.
     */
    object Default : LoginState

    /**
     * The login is ongoing.
     */
    object Loading : LoginState
}