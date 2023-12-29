package g58089.mobg5.stible.ui.screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import g58089.mobg5.stible.R
import g58089.mobg5.stible.model.dto.GameRules
import g58089.mobg5.stible.model.dto.GuessResponse
import g58089.mobg5.stible.model.dto.Route
import g58089.mobg5.stible.model.network.RequestState
import g58089.mobg5.stible.model.util.ErrorType
import g58089.mobg5.stible.model.util.GameState
import g58089.mobg5.stible.ui.STIBleViewModelProvider
import g58089.mobg5.stible.ui.theme.Green
import g58089.mobg5.stible.ui.theme.Yellow
import java.util.Locale

const val TAG = "GameScreen"

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameScreenViewModel = viewModel(factory = STIBleViewModelProvider.Factory)
) {
    GameScreenBody(
        gameRules = viewModel.gameRules,
        userGuess = viewModel.userGuess,
        canStillPlay = viewModel.canGuess,
        guessHistory = viewModel.madeGuesses,
        gameState = viewModel.gameState,
        mysteryStop = viewModel.mysteryStop,
        bestPercentage = viewModel.highestProximity,
        onUserGuessChange = viewModel::changeGuess,
        onGuess = viewModel::guess,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(id = R.dimen.main_padding))
    )


    val requestState = viewModel.requestState

    if (requestState is RequestState.Error) {
        val errorMsgId = when (requestState.error) {
            ErrorType.GAME_OVER -> R.string.error_game_over
            ErrorType.NO_INTERNET -> R.string.error_no_internet
            ErrorType.NEW_LEVEL_AVAILABLE -> R.string.error_new_level_available
            ErrorType.BAD_LANGUAGE -> R.string.error_bad_language
            ErrorType.BAD_STOP -> R.string.error_bad_stop
            ErrorType.UNKNOWN -> R.string.error_unknown
        }
        Toast.makeText(LocalContext.current, stringResource(id = errorMsgId), Toast.LENGTH_SHORT)
            .show()
    }
}

@Composable
fun GameScreenBody(
    gameRules: GameRules,
    userGuess: String,
    canStillPlay: Boolean,
    guessHistory: List<GuessResponse>,
    gameState: GameState,
    mysteryStop: String?,
    bestPercentage: Double,
    onUserGuessChange: (String) -> Unit,
    onGuess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        /* ROUTES */

        Row {
            repeat(gameRules.puzzleRoutes.size) {
                RouteSquare(
                    gameRules.puzzleRoutes[it],
                    Modifier.padding(dimensionResource(R.dimen.main_padding))
                )
            }
        }

        /* GUESS ROWS */

        Column {
            repeat(gameRules.maxGuessCount) {
                GuessRow(guessResponse = guessHistory.getOrNull(it))
            }
        }

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.main_padding)))

        /* GUESS INPUT */

        StopSearchBar(
            userGuess, onUserGuessChange, gameRules.stops, canStillPlay, Modifier.fillMaxWidth()
        )

        /* BIG INTERACTION BUTTON */

        if (gameState != GameState.WON && gameState != GameState.LOST) {
            Button(onClick = onGuess, Modifier.fillMaxWidth(), enabled = canStillPlay) {
                Text(text = stringResource(id = R.string.guess))
            }
        } else {

            val shareText = buildShareMessage(
                puzzleNumber = gameRules.puzzleNumber,
                maxGuessCount = gameRules.maxGuessCount,
                guessHistory = guessHistory,
                bestPercentage = bestPercentage,
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
        }

        /* POST GAME MESSAGE */

        if (mysteryStop != null) {
            val gameOverMsg =
                if (gameState == GameState.LOST) R.string.stop_name_lost else R.string.won

            Text(
                text = stringResource(id = gameOverMsg, mysteryStop),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else if (gameState in arrayOf(GameState.LOST, GameState.WON)) {
            Log.e(TAG, "Game is over, but no mystery stop was provided by the backend.")
        }
    }
}


/**
 * Displays a [GuessResponse] to the player.
 *
 * Format :
 * Guessed stop name | Percentage indicators (squares) | Distance | Direction
 *
 * @param guessResponse the [GuessResponse] to display, or `null` for a blank row
 */
@Composable
fun GuessRow(guessResponse: GuessResponse?, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(vertical = dimensionResource(R.dimen.guess_row_padding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.guess_row_padding))
    ) {
        GuessRowTextCell(
            guessResponse?.stopName,
            Modifier.fillMaxWidth(0.4f)
        )
        GuessRowPercentageSquares(
            guessResponse?.proximityPecentage,
            Modifier.size(dimensionResource(R.dimen.guess_row_height))
        )

        val distanceText =
            guessResponse?.distance?.let { String.format(Locale.ENGLISH, "%.1fkm", it) }
        GuessRowTextCell(
            text = distanceText,
            Modifier.weight(1f)
        )

        // TODO: put a tooltip for small screens that can't display the whole thing

        val colorDirection = getDirectionBackgroundColor(guessResponse)
        val vector = guessResponse?.directionEmoji?.let { emojiToIcon(it) }
        GuessRowIcon(
            icon = vector,
            bgColor = colorDirection,
            modifier = Modifier.size(dimensionResource(R.dimen.guess_row_height))
        )
    }
}

/**
 * Displays a text in a cell of a [GuessRow].
 *
 * Should be used in a [GuessRow]
 */
@Composable
private fun GuessRowTextCell(text: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
            .background(MaterialTheme.colorScheme.outline)
            .height(dimensionResource(R.dimen.guess_row_height))

    ) {
        text?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Displays the proximity percentage as a list of 5 squares.
 *
 * Green square : 20% of proximity
 * Yellow square : 10% of proximity
 * Black square : 0% of proximity
 */
@Composable
private fun GuessRowPercentageSquares(proximityPercentage: Double?, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.guess_row_padding))) {
        val nbOfGreen: Int = proximityPercentage?.times(100)?.div(20)?.toInt() ?: 0
        val nbOfYellow: Int =
            proximityPercentage?.times(100)?.rem(20)?.div(10)?.toInt() ?: 0
        repeat(5) { sqNb ->
            val color: Color = if (sqNb < nbOfGreen) Green
            else if (sqNb < nbOfGreen + nbOfYellow) Yellow
            // if there are 2 green 1 yellow, yellow starts at 3
            else MaterialTheme.colorScheme.surfaceVariant
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
                    .background(color)
            )
        }
    }
}

@Composable
private fun GuessRowIcon(icon: ImageVector?, bgColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
            .background(bgColor)
    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = it.name)
        }
    }
}

/**
 * Small little helper for the direction background color.
 *
 * - No [GuessResponse] was passed : usual gray
 * - Non-winning guess : blue
 * - Winning guess : green
 *
 * @param guess the [GuessResponse] of this guess row, null if the row is empty
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

/**
 * Searchable field for stops.
 *
 * @param userGuess the stored input to display upon recomposition
 * @param onUserGuessChange the function to call when the user inputs something
 * @param allStops the list of possible stops available to the player
 * @param guessEnabled false if the player shouldn't be able to search a stop
 */
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
        val filteredStops = allStops.filter { it.contains(userGuess, ignoreCase = true) }
        // FIXME: accents don't pass, so you have to actually write them
        // which is the behaviour in the online game so....
        if (filteredStops.isNotEmpty()) {
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                expanded = false
            }) {

                // NOTE: bug in Jetpack Compose : i MUST put fixed size for
                // LazyColumn inside ExposedDropdownMenu
                Box(
                    modifier = Modifier
                        .width(500.dp)
                        .height(300.dp)
                ) {
                    LazyColumn {
                        items(filteredStops) {
                            DropdownMenuItem(onClick = {
                                onUserGuessChange(it)
                                expanded = false
                            }, text = {
                                Text(text = it)
                            })
                        }
                    }
                }
            }
        }
    }
}

/**
 * Displays a [Route] as a rounded square, like on the STIB website.
 *
 * @param route the [Route] to display
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


/**
 * Converts a [GuessResponse] to a sequence of square emojis.
 *
 * Basically takes the squares shown on the screen and converts them to text.
 */
private fun buildSquaresForShare(guess: GuessResponse): String {
    val percentage = guess.proximityPecentage.times(100).toInt()
    val green = percentage.div(20)
    val yellow = percentage.rem(20).div(10)

    return "${"🟩".repeat(green)}${"🟨".repeat(yellow)}${"⬛".repeat(5 - green - yellow)} ${guess.directionEmoji}"

}

/**
 * Generates the shareable "post-mortem" for this play session.
 *
 * A standard post-mortem looks like the following :
 *
 * STIBle #123 3/6 (100%)
 *
 * 🟩⬛⬛🟨⬛
 * 🟩🟩🟩🟩⬛
 * 🟩🟩🟩🟩🟩
 *
 * STIBle App - https://stible.elitios.net/
 *
 * @param puzzleNumber today's puzzle number
 * @param maxGuessCount the amount of times the user can guess in a day
 * @param guessHistory all [GuessResponse] received during the session
 * @param bestPercentage the proximity percentage of the guess closest to the mystery stop
 * @param gameState whether the user has lost or not //TODO: this should be a boolean
 */
@Composable
private fun buildShareMessage(
    puzzleNumber: Int,
    maxGuessCount: Int,
    bestPercentage: Double,
    guessHistory: List<GuessResponse>,
    gameState: GameState
): String {
    // i've thought of not letting the user share if the game isn't finished, but who cares honestly.

    val nbTries = if (gameState == GameState.LOST) "X" else guessHistory.size

    val squares = StringBuilder()

    guessHistory.forEach {
        squares.append(buildSquaresForShare(it)).append('\n')
    }

    return """
${stringResource(id = R.string.app_name)} #${puzzleNumber} $nbTries/$maxGuessCount (${
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
fun GameScreenBodyPreview() {
    GameScreenBody(
        gameRules = GameRules(),
        userGuess = "",
        canStillPlay = true,
        guessHistory = emptyList(),
        gameState = GameState.PLAYING,
        mysteryStop = null,
        bestPercentage = 0.0,
        onUserGuessChange = {},
        onGuess = {},
        modifier = Modifier.fillMaxSize()
    )
}