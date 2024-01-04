package g58089.mobg5.stible.ui.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import g58089.mobg5.stible.R
import g58089.mobg5.stible.data.util.ErrorType

@Composable
fun ShowToast(error: ErrorType) {
    Toast.makeText(
        LocalContext.current,
        stringResource(id = getErrorStringResId(error)),
        Toast.LENGTH_SHORT
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