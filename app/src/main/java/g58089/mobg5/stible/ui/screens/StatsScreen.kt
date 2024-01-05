package g58089.mobg5.stible.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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

    Column(modifier = modifier.padding(dimensionResource(id = R.dimen.outer_padding))) {

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