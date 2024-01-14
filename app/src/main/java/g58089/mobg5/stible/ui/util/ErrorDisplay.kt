package g58089.mobg5.stible.ui.util

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import g58089.mobg5.stible.R
import g58089.mobg5.stible.data.util.ErrorType

@Composable
fun ShowToast(error: ErrorType) {
    Toast.makeText(
        LocalContext.current, stringResource(id = getErrorStringResId(error)), Toast.LENGTH_SHORT
    ).show()
}

/**
 * Fetches the error messages for each [ErrorType]
 */
fun getErrorStringResId(errorType: ErrorType): Int {
    return when (errorType) {
        ErrorType.GAME_OVER -> R.string.error_game_over
        ErrorType.NO_INTERNET -> R.string.error_no_internet
        ErrorType.NEW_LEVEL_AVAILABLE -> R.string.error_new_level_available
        ErrorType.BAD_LANGUAGE -> R.string.error_bad_language
        ErrorType.BAD_STOP -> R.string.error_bad_stop
        ErrorType.UNKNOWN -> R.string.error_unknown
        ErrorType.TRANSLATION_FAILURE -> R.string.error_translation_failure
    }
}

/**
 * Displays an error message according to a provided [ErrorType] and a reload button.
 */
@Composable
fun ErrorMessageScreen(errorType: ErrorType, onReload: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var icon = Icons.Default.Error

        if (errorType == ErrorType.NO_INTERNET) {
            icon = Icons.Default.WifiOff
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(id = R.dimen.error_icon))
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.outer_padding)))
        Text(text = stringResource(id = getErrorStringResId(errorType)))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.outer_padding)))
        Button(onClick = onReload) {
            Text(text = stringResource(id = R.string.reload))
        }
    }
}