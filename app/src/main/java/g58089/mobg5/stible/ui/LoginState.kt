package g58089.mobg5.stible.ui

import g58089.mobg5.stible.model.AuthUser
import g58089.mobg5.stible.model.ErrorType

sealed interface LoginState {
    data class Success(val authUser: AuthUser) : LoginState
    data class Error(val error: ErrorType) : LoginState
    object Default : LoginState
}