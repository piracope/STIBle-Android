package g58089.mobg5.stible.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import g58089.mobg5.stible.R
import g58089.mobg5.stible.data.network.RequestState
import g58089.mobg5.stible.ui.STIBleViewModelProvider
import g58089.mobg5.stible.ui.util.ShowToast

/**
 * Shows a screen to let the user choose his settings.
 *
 * Settings :
 * - switch button to enable/disable map mode
 * - switch button to toggle between French and Dutch
 * - button to remove all data
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsScreenViewModel = viewModel(factory = STIBleViewModelProvider.Factory)
) {
    SettingsScreenBody(
        isMapModeEnabled = viewModel.isMapModeEnabled,
        canChangeMapMode = viewModel.canChangeMapMode,
        onMapModeChange = viewModel::changeMapMode,
        onClearAll = viewModel::removeAllData,
        nederlands = viewModel.isInNederlands,
        switchToNl = viewModel::changeToNederlands,
        modifier.fillMaxSize()
    )

    val requestState = viewModel.requestState

    if (requestState is RequestState.Error) {
        ShowToast(error = requestState.error)
    }
}

@Composable
private fun SettingsScreenBody(
    isMapModeEnabled: Boolean,
    canChangeMapMode: Boolean,
    onMapModeChange: (Boolean) -> Unit,
    onClearAll: () -> Unit,
    nederlands: Boolean,
    switchToNl: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isRemoveDialogOpen by rememberSaveable { mutableStateOf(false) }

    Column(modifier, verticalArrangement = Arrangement.Top) {
        SwitchSetting(
            title = stringResource(id = R.string.map_mode_title),
            subtitle = stringResource(id = R.string.map_mode_subtitle),
            checked = isMapModeEnabled,
            onChange = onMapModeChange,
            enabled = canChangeMapMode,
            modifier = Modifier.fillMaxWidth()
        )
        SwitchSetting(
            title = stringResource(id = R.string.nl_mode_title),
            subtitle = stringResource(id = R.string.nl_subtitle),
            checked = nederlands,
            onChange = switchToNl,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.weight(1f))

        FilledTonalButton(
            onClick = { isRemoveDialogOpen = !isRemoveDialogOpen },
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

        if (isRemoveDialogOpen) {
            AlertDialog(icon = {
                Icon(
                    imageVector = Icons.Default.DeleteForever, contentDescription = null
                )
            }, title = {
                Text(text = stringResource(id = R.string.remove_all_data_dialog_title))
            }, text = {
                Text(text = stringResource(id = R.string.remove_all_data_dialog_text))
            }, onDismissRequest = { isRemoveDialogOpen = false }, confirmButton = {
                TextButton(onClick = {
                    isRemoveDialogOpen = false
                    onClearAll()
                }) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            }, dismissButton = {
                TextButton(onClick = { isRemoveDialogOpen = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            })
        }
    }
}

/**
 * Toggleable setting.
 *
 * Shows a title and subtitle.
 */
@Composable
fun SwitchSetting(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier
            .toggleable(checked, enabled = enabled, onValueChange = onChange)
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
        Switch(
            checked = checked, onCheckedChange = onChange, enabled = enabled
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenBodyPreview() {
    SettingsScreenBody(isMapModeEnabled = false,
        onMapModeChange = {},
        onClearAll = {},
        nederlands = false,
        canChangeMapMode = true,
        switchToNl = {})
}