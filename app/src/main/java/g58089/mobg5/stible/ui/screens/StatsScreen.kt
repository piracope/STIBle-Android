package g58089.mobg5.stible.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.VerticalPosition
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import g58089.mobg5.stible.R
import g58089.mobg5.stible.ui.STIBleViewModelProvider

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsScreenViewModel = viewModel(factory = STIBleViewModelProvider.Factory)
) {
    val currentChartValues = mutableListOf<FloatEntry>()

    viewModel.guessCountRepartition.entries.forEach {
        currentChartValues.add(FloatEntry(it.key.toFloat(), it.value.toFloat()))
    }

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.outer_padding)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card {
            Chart(
                chart = columnChart(
                    innerSpacing = dimensionResource(id = R.dimen.inner_padding),
                    spacing = dimensionResource(id = R.dimen.inner_padding),
                    columns = listOf(
                        lineComponent(
                            color = MaterialTheme.colorScheme.primary,
                            thickness = DefaultDimens.COLUMN_WIDTH.dp,
                            shape = Shapes.roundedCornerShape(
                                DefaultDimens.COLUMN_ROUNDNESS_PERCENT,
                                DefaultDimens.COLUMN_ROUNDNESS_PERCENT,
                                0,
                                0
                            )
                        )
                    ),
                    dataLabel = textComponent {
                        color = MaterialTheme.colorScheme.onPrimary.toArgb()
                        textSizeSp = MaterialTheme.typography.titleMedium.fontSize.value
                    },
                    dataLabelVerticalPosition = VerticalPosition.Bottom,
                    dataLabelValueFormatter = getAxisFormatterReplacingFloatWithString(
                        0f,
                        ""
                    )
                ),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.inner_padding)),
                model = entryModelOf(currentChartValues),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = getAxisFormatterReplacingFloatWithString(
                        StatsScreenViewModel.FLOAT_LOSS_MARKER,
                        stringResource(id = R.string.loss_marker)
                    ),
                    title = stringResource(id = R.string.guess_distribution),
                    titleComponent = textComponent {
                        color = MaterialTheme.colorScheme.onSurface.toArgb()
                        textSizeSp = MaterialTheme.typography.titleMedium.fontSize.value
                    }
                ),
                isZoomEnabled = false,
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.outer_padding)))

        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.inner_padding))) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.inner_padding)),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatsCard(
                    value = viewModel.numberOfGames.toString(),
                    label = stringResource(id = R.string.number_of_games),
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    value = viewModel.winRate.times(100).toInt().toString() + "%",
                    label = stringResource(id = R.string.win_rate),
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.inner_padding)),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatsCard(
                    value = viewModel.currentStreak.toString(),
                    label = stringResource(id = R.string.current_streak),
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    value = viewModel.bestStreak.toString(),
                    label = stringResource(id = R.string.best_streak),
                    modifier = Modifier.weight(1f)
                )
            }
        }

    }


}

@Composable
private fun StatsCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.inner_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
            )
            Text(text = label)
        }
    }
}

private fun getAxisFormatterReplacingFloatWithString(
    floatToReplace: Float,
    replacementString: String
): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    return AxisValueFormatter { value, _ ->
        if (value == floatToReplace) replacementString else value.toInt().toString()
    }
}