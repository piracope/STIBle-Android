package g58089.mobg5.remise1.ui

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class Remise1ViewModel : ViewModel() {
    var uiState by mutableStateOf(Remise1State())
        private set

    var userEmail by mutableStateOf("")
        private set

    init {
        uiState.userEmail = userEmail
    }

    fun updateUserEmail(input: String) {
        userEmail = input
    }

    fun checkUserEmail() {
        val isEmailWrong =
            userEmail.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()

        uiState = uiState.copy(isEmailWrong = isEmailWrong, isLoginSuccessful = !isEmailWrong)
    }
}