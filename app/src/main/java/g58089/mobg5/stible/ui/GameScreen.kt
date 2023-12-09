package g58089.mobg5.stible.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import g58089.mobg5.stible.R
import g58089.mobg5.stible.model.dto.GameRules
import g58089.mobg5.stible.model.dto.Route


@Composable
fun GameScreen(
    gameRules: GameRules,
    userGuess: String,
    onUserGuessChange: (String) -> Unit,
    onGuess: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Displaying the routes
        Row {
            repeat(gameRules.puzzleRoutes.size) {
                val currentRoute = gameRules.puzzleRoutes[it]
                RouteSquare(currentRoute, Modifier.padding(dimensionResource(R.dimen.main_padding)))
            }
        }
        GuessRows(gameRules.maxGuessCount, Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.main_padding)))
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            StopSearchBar(userGuess, onUserGuessChange, gameRules.stops, Modifier.fillMaxWidth())
            OutlinedButton(onClick = onGuess, Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.guess))
            }
        }

    }
}


@Composable
fun GuessRows(maxGuessCount: Int, modifier: Modifier = Modifier) {
    // for each guess possible
    Column(modifier = modifier) {
        repeat(maxGuessCount) {
            // we're gonna have a row
            Row(
                modifier = modifier
                    .padding(vertical = dimensionResource(R.dimen.guess_row_padding)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.guess_row_padding))
            ) {
                // of "fields"
                // FIXME: not very DRY

                // the GuessField
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                        .background(MaterialTheme.colorScheme.outline)
                        .height(dimensionResource(R.dimen.guess_row_height))
                        .fillMaxWidth(0.4f)
                )
                // 5 squares for the results
                Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.guess_row_padding))) {
                    repeat(5) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .size(dimensionResource(R.dimen.guess_row_height))
                        )
                    }
                }

                // the distance
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                        .background(MaterialTheme.colorScheme.outline)
                        .height(dimensionResource(R.dimen.guess_row_height))
                        .weight(1f)
                )

                // and the direction
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                        .background(MaterialTheme.colorScheme.outline)
                        .size(dimensionResource(R.dimen.guess_row_height))
                )
            }
        }
    }

}

@Composable
fun StopSearchBar(
    userGuess: String,
    onUserGuessChange: (String) -> Unit,
    allStops: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // SearchableComboBox like in JavaFX
    // FIXME: lots of stops so it struggles upon expanding
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            modifier = modifier.menuAnchor(), //idk, needed for Material3
            value = userGuess,
            onValueChange = onUserGuessChange,
            placeholder = { Text(stringResource(R.string.input_placeholder)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )
        // filter options based on text field value
        val filteredStops =
            allStops.filter { it.contains(userGuess, ignoreCase = true) }
        if (filteredStops.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                filteredStops.forEach { selectedStop ->
                    DropdownMenuItem(
                        onClick = {
                            onUserGuessChange(selectedStop)
                            expanded = false
                        },
                        text = {
                            Text(text = selectedStop)
                        }
                    )
                }
            }
        }

    }
}

/**
 * Displays a route as a rounded square, like on the STIB website.
 */
@Composable
private fun RouteSquare(route: Route, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
            .background(Color(getColorFromRRGGBB(route.routeColor)))
            .size(dimensionResource(R.dimen.route_logo))
    ) {
        Text(
            text = route.routeNumber.toString(),
            color = Color(getColorFromRRGGBB(route.routeNumberColor)),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(R.dimen.route_number_size).value.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * Converts an "RRGGBB" string to the color's int representation.
 */
private fun getColorFromRRGGBB(colorStr: String): Int {
    return "#$colorStr".toColorInt()
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen(
        gameRules = GameRules(),
        modifier = Modifier.fillMaxSize(),
        userGuess = "",
        onUserGuessChange = {},
        onGuess = {})
}