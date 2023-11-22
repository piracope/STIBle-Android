package g58089.mobg5.stible.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import g58089.mobg5.stible.R
import g58089.mobg5.stible.model.ErrorType

/**
 * Displays the Login Screen.
 *
 * The Login Screen shows two elements of importance to the user :
 *  - the email field
 *  - the password field
 *  - the validation button
 *
 * The state of the user field is NOT persisted through this Composable, it must be stored
 * using the ViewModel through a lambda function passed as `onEmailChange` and `onPasswordChange`.
 *
 * When the user clicks on the validation button, the user's input is handled by the `onLoginAttempt`
 * lambda function. Upon login, the `loginState` should be updated accordingly and passed to this
 * composable.
 *
 * @param email the state of the user-provided email to show on the screen
 * @param password the state of the user-provided password to show on the screen
 * @param loginState the current state of the login process
 * @param onEmailChange function called at each change of the user email input
 * @param onPasswordChange function called at each change of the user password input
 * @param onLoginAttempt function called at each press of the confirmation button
 * @param onNavigateLoginSuccess function called when login is verified to navigate to the next screen
 */
@Composable
fun LoginScreen(
    email: String,
    password: String,
    loginState: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginAttempt: () -> Unit,
    onNavigateLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(key1 = loginState) {
        if (loginState is LoginState.Success) {
            onNavigateLoginSuccess()
        }
    }

    // find out which fields should we set as having an error

    var errorEmail = false
    var errorPassword = false
    var generalError = false

    if (loginState is LoginState.Error) {
        when (loginState.error) {
            ErrorType.BAD_EMAIL_FORMAT -> errorEmail = true
            ErrorType.NO_PASSWORD -> errorPassword = true
            ErrorType.NO_INTERNET -> generalError = true
            ErrorType.BAD_CREDENTIALS -> {
                errorEmail = true
                errorPassword = true
                generalError = true
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // email text field
        TextField(
            value = email,
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.login_email_hint))
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.MailOutline, contentDescription = null)
            },
            onValueChange = onEmailChange,
            isError = errorEmail,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            supportingText = {
                if (loginState is LoginState.Error) {
                    if (loginState.error == ErrorType.BAD_EMAIL_FORMAT) {
                        Text(text = stringResource(id = R.string.login_bad_email_format))
                    }
                    // NOTE: i'm nesting the if instead of having a longer condition because
                    // other email field error types could appear later
                }
            }
        )

        // password text field
        TextField(
            value = password,
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.login_password_hint))
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = errorPassword,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            onValueChange = onPasswordChange,
            keyboardActions = KeyboardActions(
                onDone = { onLoginAttempt() }
            ),
            supportingText = {
                if (loginState is LoginState.Error) {
                    if (loginState.error == ErrorType.NO_PASSWORD) {
                        Text(text = stringResource(id = R.string.login_no_password))
                    }
                }
            },
        )
        if (generalError && loginState is LoginState.Error) {
            // the second condition is useless, but i have to do it to get access
            // to the loginState's error attribute

            if (loginState.error == ErrorType.BAD_CREDENTIALS) {
                Text(text = stringResource(id = R.string.login_bad_credentials))
            } else { // Android Studio cried when i explicitly stated the error type
                Text(text = stringResource(id = R.string.login_no_internet))
            }
        }

        // Connection Button, disabled when a login has not finished yet
        Button(
            onClick = onLoginAttempt,
            enabled = loginState !is LoginState.Loading,
        ) {
            Text(text = stringResource(id = R.string.login_button))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        email = "",
        password = "",
        loginState = LoginState.Default,
        onLoginAttempt = {},
        onEmailChange = {},
        onPasswordChange = {},
        onNavigateLoginSuccess = {},
        modifier = Modifier.fillMaxHeight()
    )
}