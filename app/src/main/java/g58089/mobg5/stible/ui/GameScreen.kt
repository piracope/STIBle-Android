package g58089.mobg5.stible.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import g58089.mobg5.stible.model.GameRules
import g58089.mobg5.stible.model.Route


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
private fun RouteSquare(route: Route) {
    Box(
        modifier = Modifier
            .padding(8.dp) // TODO: set dimens in resources
            .clip(shape = RoundedCornerShape(3.dp))
            .background(Color(getColorFromRRGGBB(route.routeColor)))
            .size(32.dp)
    ) {
        Text(
            text = route.routeNumber.toString(),
            color = Color(getColorFromRRGGBB(route.routeNumberColor)),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun GameScreen(gameRules: GameRules, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // Displaying the routes
        Row(Modifier.padding(8.dp)) {
            repeat(gameRules.puzzleRoutes.size) {
                val currentRoute = gameRules.puzzleRoutes[it]
                RouteSquare(currentRoute)
            }
        }

        Log.d("GameScreen", "guessCount = ${gameRules.maxGuessCount}")
        // Displaying the guesses
        Column {
            repeat(gameRules.maxGuessCount) {
                Row(
                    modifier = Modifier.padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // TODO: figure out how to correctly set the width
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.outline)
                            .size(16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.outline)
                            .size(16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.outline)
                            .size(16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.outline)
                            .size(16.dp)
                    )
                }
            }
        }
    }

}