package g58089.mobg5.stible.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import g58089.mobg5.stible.R
import g58089.mobg5.stible.model.dto.GameRules
import g58089.mobg5.stible.model.dto.Route


/**
 * Converts an "RRGGBB" string to the color's int representation.
 */
private fun getColorFromRRGGBB(colorStr: String): Int {
    return "#$colorStr".toColorInt()
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

@Composable
fun GameScreen(gameRules: GameRules, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(dimensionResource(R.dimen.main_padding))) {
        // Displaying the routes
        Row {
            repeat(gameRules.puzzleRoutes.size) {
                val currentRoute = gameRules.puzzleRoutes[it]
                RouteSquare(currentRoute, Modifier.padding(dimensionResource(R.dimen.main_padding)))
            }
        }
        // Displaying the guesses
        Column {
            // for each guess possible
            repeat(gameRules.maxGuessCount) {
                // we're gonna have a row
                Row(
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.guess_row_padding))
                        .fillMaxWidth(),
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
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen(gameRules = GameRules(), modifier = Modifier.fillMaxSize())
}