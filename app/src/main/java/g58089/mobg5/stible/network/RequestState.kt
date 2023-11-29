package g58089.mobg5.stible.network

import g58089.mobg5.stible.model.util.ErrorType

sealed interface RequestState {

    /**
     * An error occurred.
     */
    data class Error(val error: ErrorType) : RequestState

    /**
     * A request is currently ongoing.
     */
    object Loading : RequestState

    /**
     * Nothing is happening.
     */
    object Default : RequestState
}