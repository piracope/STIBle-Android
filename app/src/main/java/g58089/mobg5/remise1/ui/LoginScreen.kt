package g58089.mobg5.remise1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import g58089.mobg5.remise1.R

/**
 * Displays the Login Screen.
 *
 * The Login Screen shows two elements of importance to the user :
 *  - the email field
 *  - the validation button
 *
 * The state of the user field is NOT persisted through this Composable, it must be stored
 * using the ViewModel through a lambda function passed as `onEmailChange`.
 *
 * When the user clicks on the validation button, the user's input is handled by the `onLoginAttempt`
 * lambda function. If successful, the user should now be logged in and be sent to the next screen
 * according to the app's navigation logic, which is passed here using the `onNavigateLoginSuccess`
 * lambda function. If the login isn't successful, `isEmailWrong` should be set to true, which
 * will be shown as an error in the email field.
 *
 * @param email the state of the user-provided email to show on the screen
 * @param isEmailWrong true if the last login attempt was unsuccessful
 * @param isLoginSuccessful true if the last login attempt was successful
 * @param onEmailChange function called at each change of the user email input
 * @param onLoginAttempt function called at each press of the confirmation button
 * @param onNavigateLoginSuccess function called once the login is successful
 */
@Composable
fun LoginScreen(
    email: String,
    isEmailWrong: Boolean,
    isLoginSuccessful: Boolean,
    onEmailChange: (String) -> Unit,
    onLoginAttempt: () -> Unit,
    onNavigateLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {

    //FIXME: this doesn't work very well -> weird double navigation. i need to ask QHB
    if (isLoginSuccessful) {
        LaunchedEffect(email) {
            onNavigateLoginSuccess()
        }
    }

    /*
    +------------------------+---------------------+
    | Email Field            | Confirmation button |
    +------------------------+---------------------+
    | Eventual error message                       |
    +------------------------+---------------------+
     */
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextField(
                value = email,
                singleLine = true,
                label = {
                    Text(text = stringResource(id = R.string.login_email_hint))
                },
                onValueChange = onEmailChange,
                isError = isEmailWrong,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onLoginAttempt() }
                ),
                supportingText = {
                    if (isEmailWrong) {
                        Text(
                            text = stringResource(id = R.string.login_error)
                        )
                    }
                }
            )
            Button(onClick = onLoginAttempt) {
                Text(text = stringResource(id = R.string.login_button))
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        email = "",
        isEmailWrong = true,
        isLoginSuccessful = false,
        onLoginAttempt = {},
        onEmailChange = {},
        onNavigateLoginSuccess = {},
        modifier = Modifier.fillMaxHeight()
    )
}