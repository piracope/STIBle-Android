package g58089.mobg5.stible.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import g58089.mobg5.stible.R
import g58089.mobg5.stible.ui.STIBleViewModelProvider

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsScreenViewModel = viewModel(factory = STIBleViewModelProvider.Factory)
) {
    SettingsScreenBody(
        isMapModeEnabled = viewModel.isMapModeEnabled,
        onMapModeChange = viewModel::changeMapMode,
        onClearAll = viewModel::removeAllData,
        modifier
            .fillMaxSize()
    )
}

@Composable
fun SettingsScreenBody(
    isMapModeEnabled: Boolean,
    onMapModeChange: (Boolean) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        SwitchSetting(
            title = stringResource(id = R.string.map_mode_title),
            subtitle = stringResource(id = R.string.map_mode_subtitle),
            checked = isMapModeEnabled,
            onChange = onMapModeChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))

        FilledTonalButton(
            onClick = onClearAll,
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.outer_padding))
        ) {
            Text(
                text = stringResource(id = R.string.remove_all_data_button),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun SwitchSetting(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .toggleable(checked, onValueChange = onChange)
            .padding(dimensionResource(id = R.dimen.outer_padding)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}