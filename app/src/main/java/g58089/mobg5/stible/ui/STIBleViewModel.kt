package g58089.mobg5.stible.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel

/**
 * The ViewModel handling all business logic in the application.
 */
class STIBleViewModel : ViewModel() {
    /**
     * The current state of data to be used by the UI.
     */
    var uiState by mutableStateOf(STIBleState())
        private set

    /**
     * The email address provided by the user.
     *
     * This should be kept updated with each user input.
     */
    var userEmail by mutableStateOf("")
        private set

    /**
     * Updates the ViewModel's stored user email with a user-provided input.
     *
     * This method should be called to keep track of any change in the email input.
     *
     * @param input the user's email address
     */
    fun updateUserEmail(input: String) {
        userEmail = input
    }

    /**
     * Checks if the user's email is actually an email address, and logs them in if it is. Declares
     * that the email is wrong if wrongly formatted.
     *
     * This method should be called only once the user validates their input.
     */
    fun checkUserEmail() {
        // thanks stackoverflow
        val isEmailWrong =
            userEmail.isBlank() || !PatternsCompat.EMAIL_ADDRESS.matcher(userEmail).matches()

        uiState = uiState.copy(
            userEmail = userEmail,
            isEmailWrong = isEmailWrong,
            isLoginSuccessful = !isEmailWrong
        )
    }
}