package g58089.mobg5.stible.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import g58089.mobg5.stible.R

/**
 * Displays the logo of the HE2B-ESI, vertically centered.
 */
@Composable
fun LogoScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(
                id = R.string.logo_description
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun LogoScreenPreview() {
    LogoScreen(modifier = Modifier.fillMaxHeight())
}