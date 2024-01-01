package g58089.mobg5.stible.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.carlsen.flagkit.FlagIcons
import dev.carlsen.flagkit.flagicons.FR
import dev.carlsen.flagkit.flagicons.NL
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
        nederlands = viewModel.isInNederlands,
        switchToNl = viewModel::changeToNederlands,
        modifier
            .fillMaxSize()
    )
}

@Composable
fun SettingsScreenBody(
    isMapModeEnabled: Boolean,
    onMapModeChange: (Boolean) -> Unit,
    onClearAll: () -> Unit,
    nederlands: Boolean,
    switchToNl: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.Top) {
        SwitchSetting(
            title = stringResource(id = R.string.map_mode_title),
            subtitle = stringResource(id = R.string.map_mode_subtitle),
            checked = isMapModeEnabled,
            onChange = onMapModeChange,
            modifier = Modifier.fillMaxWidth()
        )
        SwitchSetting(
            title = stringResource(id = R.string.lang_picker_title),
            subtitle = stringResource(id = R.string.lang_picker_subtitle),
            //onLabel = "\uD83C\uDDF3\uD83C\uDDF1", // NL flag
            //offLabel = "\uD83C\uDDEB\uD83C\uDDF7", // FR flag
            checked = nederlands,
            onChange = switchToNl,
            modifier = Modifier.fillMaxWidth(),
            hasCustomHandleIcon = true,
            handleIcon = {
                val flag =
                    if (nederlands) {
                        FlagIcons.NL
                    } else {
                        FlagIcons.FR
                    }
                Image(
                    imageVector = flag,
                    contentDescription = flag.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(SwitchDefaults.IconSize)
                        .clip(CircleShape)
                )
            }
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
        } // TODO: alert


    }
}

@Composable
fun SwitchSetting(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onLabel: String? = null,
    offLabel: String? = null,
    hasCustomHandleIcon: Boolean = false,
    handleIcon: @Composable () -> Unit = {}
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
        offLabel?.let {
            Text(text = offLabel)
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.inner_padding)))
        }
        if (hasCustomHandleIcon) {
            Switch(
                checked = checked,
                onCheckedChange = onChange,
                thumbContent = handleIcon
            )
        } else {
            Switch(
                checked = checked,
                onCheckedChange = onChange,
            )
        }
        onLabel?.let {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.inner_padding)))
            Text(text = onLabel)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenBodyPreview() {
    SettingsScreenBody(
        isMapModeEnabled = false,
        onMapModeChange = {},
        onClearAll = {},
        nederlands = false,
        switchToNl = {}
    )
}