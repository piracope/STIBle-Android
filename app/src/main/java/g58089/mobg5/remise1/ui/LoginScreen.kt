package g58089.mobg5.remise1.ui

import android.util.Log
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

@Composable
fun LoginScreen(
    email: String,
    isEmailWrong: Boolean,
    isLoginSuccessful: Boolean,
    onEmailChange: (String) -> Unit,
    onLoginConfirmed: () -> Unit,
    onNavigateLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {

    //FIXME: this doesn't work very well -> weird double navigation. i need to ask QHB
    if (isLoginSuccessful) {
        LaunchedEffect(true) {
            Log.d("login", "we navigate")
            onNavigateLoginSuccess()
        }
    }

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
                    onDone = { onLoginConfirmed() }
                )
            )
            Button(onClick = onLoginConfirmed) {
                Text(text = stringResource(id = R.string.login_button))
            }
        }

        if (isEmailWrong) {
            Text(text = stringResource(id = R.string.login_error))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        email = "",
        isEmailWrong = false,
        isLoginSuccessful = false,
        onLoginConfirmed = {},
        onEmailChange = {},
        onNavigateLoginSuccess = {},
        modifier = Modifier.fillMaxHeight()
    )
}