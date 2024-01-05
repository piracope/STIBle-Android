package g58089.mobg5.stible.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import g58089.mobg5.stible.ui.STIBleViewModelProvider

private const val FLOAT_LOSS_MARKER = 0.0f

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsScreenViewModel = viewModel(factory = STIBleViewModelProvider.Factory)
) {

    val listOfEntriesForChart = mutableListOf<FloatEntry>()
    val axisFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        if (value == 0.0f) "X" else value.toString()
    }

    viewModel.gameRecapGuessCount.entries.forEach {
        val countInFloat = if (it.key == "X") FLOAT_LOSS_MARKER else it.key.toFloat()
        listOfEntriesForChart.add(FloatEntry(countInFloat, it.value.toFloat()))
    }

    Chart(
        chart = columnChart(),
        model = entryModelOf(listOfEntriesForChart),
        bottomAxis = rememberBottomAxis(valueFormatter = axisFormatter)
    )
}