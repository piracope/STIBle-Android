package g58089.mobg5.stible.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import g58089.mobg5.stible.R

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_author),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(text = stringResource(id = R.string.app_author_matricule))
        Text(text = stringResource(id = R.string.app_author_group))
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}