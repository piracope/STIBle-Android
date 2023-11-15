package g58089.mobg5.stible.ui

/**
 * The State held by the ViewModel and monitored by the View.
 */
data class STIBleState(
    /**
     * The user's email address.
     */
    var userEmail: String = "",
    /**
     * True if the user tried to use an incorrectly formatted email address.
     */
    var isEmailWrong: Boolean = false,
    /**
     * True if the user provided a correctly formatted email address.
     */
    var isLoginSuccessful: Boolean = false
)