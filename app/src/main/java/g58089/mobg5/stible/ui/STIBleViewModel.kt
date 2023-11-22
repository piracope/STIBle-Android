package g58089.mobg5.stible.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.model.ErrorType
import g58089.mobg5.stible.network.AuthCredentials
import g58089.mobg5.stible.network.STIBleApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * The ViewModel handling all business logic in the application.
 */
class STIBleViewModel : ViewModel() {
    /**
     * The current state of data to be used by the UI.
     */
    var loginState: LoginState by mutableStateOf(LoginState.Default)
        private set

    /**
     * The email address provided by the user.
     *
     * This should be kept updated with each user input.
     */
    var userEmail by mutableStateOf("")
        private set

    /**
     * The password provided by the user.
     *
     * This should be kept updated with each user input.
     */
    var userPassword by mutableStateOf("")
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
     * Updates the ViewModel's stored user password with a user-provided input.
     *
     * This method should be called to keep track of any change in the password input.
     *
     * @param input the user's password
     */
    fun updateUserPassword(input: String) {
        userPassword = input
    }

    /**
     * Checks if the user's email is actually an email address, and logs them in if it is. Declares
     * that the email is wrong if wrongly formatted.
     *
     * This method should be called only once the user validates their input.
     */
    fun checkUserEmail() {
        // thanks stackoverflow
        if (userEmail.isBlank() || !PatternsCompat.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            loginState = LoginState.Error(ErrorType.BAD_EMAIL_FORMAT)
            return
        }

        if (userPassword.isBlank()) {
            loginState = LoginState.Error(ErrorType.NO_PASSWORD)
            return
        }

        loginState = LoginState.Loading

        viewModelScope.launch {
            loginState = try {
                val creds = AuthCredentials(userEmail, userPassword)
                val loginResult = STIBleApi.retrofitService.auth(creds)

                // this is a useless if statement
                if (loginResult.accessToken.isNotBlank()) {
                    LoginState.Success(loginResult)
                } else {
                    LoginState.Default
                }
            } catch (e: IOException) {
                LoginState.Error(ErrorType.NO_INTERNET)
            } catch (e: HttpException) { // http code that isn't 2xx
                LoginState.Error(ErrorType.BAD_CREDENTIALS)
            }
        }
    }
}