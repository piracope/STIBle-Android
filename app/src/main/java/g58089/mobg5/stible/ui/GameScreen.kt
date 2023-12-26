package g58089.mobg5.stible.ui

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.East
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.North
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material.icons.rounded.NorthWest
import androidx.compose.material.icons.rounded.South
import androidx.compose.material.icons.rounded.SouthEast
import androidx.compose.material.icons.rounded.SouthWest
import androidx.compose.material.icons.rounded.West
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import g58089.mobg5.stible.R
import g58089.mobg5.stible.model.dto.GameRules
import g58089.mobg5.stible.model.dto.GuessResponse
import g58089.mobg5.stible.model.dto.Route
import g58089.mobg5.stible.model.util.GameState
import g58089.mobg5.stible.network.RequestState
import g58089.mobg5.stible.ui.theme.Green
import g58089.mobg5.stible.ui.theme.Yellow
import java.util.Locale
import kotlin.math.max

const val TAG = "GameScreen"

@Composable
fun GameScreen(
    gameRules: GameRules,
    userGuess: String,
    canStillPlay: Boolean,
    guessHistory: List<GuessResponse>,
    gameState: GameState,
    requestState: RequestState, // i keep it for errors, TODO: delete this comment when it's done
    mysteryStop: String?,
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
        GuessRows(
            maxGuessCount = gameRules.maxGuessCount,
            guessHistory = guessHistory,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.main_padding)))
        StopSearchBar(
            userGuess,
            onUserGuessChange,
            gameRules.stops,
            canStillPlay,
            Modifier.fillMaxWidth()
        )
        if (gameState != GameState.WON && gameState != GameState.LOST) {
            Button(onClick = onGuess, Modifier.fillMaxWidth(), enabled = canStillPlay) {
                Text(text = stringResource(id = R.string.guess))
            }
        } else {

            val shareText = buildShareMessage(
                lvlNumber = gameRules.puzzleNumber,
                maxGuessCount = gameRules.maxGuessCount,
                guessHistory = guessHistory,
                gameState = gameState
            )

            val shareHeader = "${stringResource(id = R.string.app_name)} #${gameRules.puzzleNumber}"
            val context = LocalContext.current

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(intent, shareHeader))
                },
                Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Text(text = stringResource(id = R.string.share))
            }

            // FIXME: the android tutorial hoists the share mechanic out of this composable and
            //  handles it in the STIBleScreen. Should I do that ?
        }


        if (gameState == GameState.LOST) {
            // null checking. Should not fail, but you never knooowww

            if (mysteryStop != null) {
                Text(
                    text = stringResource(id = R.string.stop_name_lost, mysteryStop),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Log.e(TAG, "Game is lost, but no mystery stop was found.")
            }
        }

        // TODO: in the original game, there is a "Bien joué !" when you won.
    }
}


@Composable
fun GuessRows(
    maxGuessCount: Int,
    guessHistory: List<GuessResponse>,
    modifier: Modifier = Modifier
) {
    // for each guess possible
    Column(modifier = modifier) {
        repeat(maxGuessCount) {
            val guess = guessHistory.getOrNull(it)

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
                ) {
                    guess?.stopName?.let { it1 ->
                        Text(
                            text = it1,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                // 5 squares for the results
                Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.guess_row_padding))) {
                    /*
                    What is the percentage ? The biggest distance between two stops is 23 km.
                    If a guess is 2.3km from the mystery stop, it is 90% close.
                    That's it : Green square : 20%, Yellow square : 10%
                    So we divide the percentage by 20 to get the number of green squares
                    and the number of yellow is percentage % 20 / 10
                     */
                    val nbOfGreen: Int = guess?.percentage?.times(100)?.div(20)?.toInt() ?: 0
                    val nbOfYellow: Int =
                        guess?.percentage?.times(100)?.rem(20)?.div(10)?.toInt() ?: 0
                    repeat(5) { sqNb ->
                        val color: Color =
                            if (sqNb < nbOfGreen)
                                Green
                            else if (sqNb < nbOfGreen + nbOfYellow)
                            // if there are 2 green 1 yellow, yellow starts at 3
                                Yellow
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                                .background(color)
                                .size(dimensionResource(R.dimen.guess_row_height))
                        )
                    }

                    // FIXME: maybe move this to another composable, this function is quite long
                }

                // the distance
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                        .background(MaterialTheme.colorScheme.outline)
                        .height(dimensionResource(R.dimen.guess_row_height))
                        .weight(1f)
                ) {
                    guess?.distance?.let { it1 ->
                        Text(
                            text = String.format(Locale.ENGLISH, "%.1fkm", it1),
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // TODO: put a tooltip for small screens that can't display the whole thing

                val colorDirection = getDirectionBackgroundColor(guess)
                // and the direction
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                        .background(colorDirection)
                        .size(dimensionResource(R.dimen.guess_row_height))
                ) {
                    guess?.let { g ->
                        val vector = emojiToIcon(g.directionEmoji)
                        Icon(imageVector = vector, contentDescription = vector.name)
                    }
                }
            }
        }
    }
}

/**
 * Small little helper for the direction color.
 *
 * If no guess was made, put the usual gray.
 * If a non winning guess is made, put the blue with the related color
 * If a winning guess is made, green !!
 */
@Composable
private fun getDirectionBackgroundColor(guess: GuessResponse?): Color {
    if (guess == null) {
        return MaterialTheme.colorScheme.outline
    }

    if (guess.directionEmoji == "✅") { // doesn't display on my machine but it's
        return Green
    }

    return MaterialTheme.colorScheme.inversePrimary
}

/**
 * Converts a direction emoji to the corresponding Material Design icon.
 */
private fun emojiToIcon(emoji: String): ImageVector {
    return when (emoji) {
        "➡️" -> Icons.Rounded.East
        "↗️" -> Icons.Rounded.NorthEast
        "⬆️" -> Icons.Rounded.North
        "↖️" -> Icons.Rounded.NorthWest
        "⬅️" -> Icons.Rounded.West
        "↙️" -> Icons.Rounded.SouthWest
        "⬇️" -> Icons.Rounded.South
        "↘️" -> Icons.Rounded.SouthEast
        "✅" -> Icons.Rounded.Check
        else -> Icons.Rounded.Error // should never happen
    }
}

@Composable
fun StopSearchBar(
    userGuess: String,
    onUserGuessChange: (String) -> Unit,
    allStops: List<String>,
    guessEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    expanded = expanded && guessEnabled

    // SearchableComboBox like in JavaFX
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },

        ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(), //idk, needed for Material3
            value = userGuess,
            onValueChange = onUserGuessChange,
            placeholder = { Text(stringResource(R.string.input_placeholder)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            singleLine = true,
            enabled = guessEnabled
        )
        // filter options based on text field value
        val filteredStops =
            allStops.filter { it.contains(userGuess, ignoreCase = true) }
        // FIXME: accents don't pass, so you have to actually write them
        // which is the behaviour in the online game so....
        if (filteredStops.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                // NOTE: bug in Jetpack Compose : i MUST put fixed size for
                // LazyColumn inside ExposedDropdownMenu
                Box(
                    modifier = Modifier
                        .width(500.dp)
                        .height(300.dp)
                ) {
                    LazyColumn {
                        items(filteredStops) {
                            DropdownMenuItem(
                                onClick = {
                                    onUserGuessChange(it)
                                    expanded = false
                                },
                                text = {
                                    Text(text = it)
                                }
                            )
                        }
                    }
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


private fun buildSquaresForShare(guess: GuessResponse): String {
    val percentage = guess.percentage.times(100).toInt()
    val green = percentage.div(20)
    val yellow = percentage.rem(20).div(10)

    return "${"🟩".repeat(green)}${"🟨".repeat(yellow)}${"⬛".repeat(5 - green - yellow)}"

}

@Composable
private fun buildShareMessage(
    lvlNumber: Int,
    maxGuessCount: Int,
    guessHistory: List<GuessResponse>,
    gameState: GameState
): String {
    val nbTries = if (gameState == GameState.LOST) "X" else guessHistory.size

    val squares = StringBuilder()
    var bestPercentage = 0.0


    guessHistory.forEach {
        squares.append(buildSquaresForShare(it)).append('\n')
        bestPercentage = max(bestPercentage, it.percentage)
    }

    return """
${stringResource(id = R.string.app_name)} #${lvlNumber} $nbTries/$maxGuessCount (${
        bestPercentage.times(
            100
        ).toInt()
    }%)

$squares

${stringResource(id = R.string.app_name)} App - https://stible.elitios.net/
    """
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen(
        gameRules = GameRules(),
        userGuess = "",
        canStillPlay = true,
        guessHistory = emptyList(),
        gameState = GameState.PLAYING,
        requestState = RequestState.Default,
        mysteryStop = null,
        onUserGuessChange = {},
        onGuess = {},
        modifier = Modifier.fillMaxSize()
    )
}