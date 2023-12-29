package g58089.mobg5.stible.data.network

import g58089.mobg5.stible.data.util.ErrorType

/**
 * Describes the state of a given HTTP request.
 */
sealed interface RequestState {

    /**
     * The request succeeded.
     */
    // FIXME: will this be used ? should i just use Default ?
    data object Success : RequestState

    /**
     * An error occurred.
     *
     * @param error the [ErrorType] that describes the error
     */
    data class Error(val error: ErrorType) : RequestState

    /**
     * A request is currently ongoing.
     */
    data object Loading : RequestState

    /**
     * Nothing is happening.
     */
    data object Default : RequestState
}