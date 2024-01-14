package g58089.mobg5.stible.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import g58089.mobg5.stible.R
import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.dto.Route
import g58089.mobg5.stible.data.dto.Stop
import g58089.mobg5.stible.data.network.RequestState
import g58089.mobg5.stible.ui.STIBleViewModelProvider
import g58089.mobg5.stible.ui.theme.STIBleBlue
import g58089.mobg5.stible.ui.theme.STIBleGreen
import g58089.mobg5.stible.ui.theme.STIBleRed
import g58089.mobg5.stible.ui.theme.light_onSTIBleGreen
import g58089.mobg5.stible.ui.util.ErrorMessageScreen
import g58089.mobg5.stible.ui.util.ShowToast
import g58089.mobg5.stible.ui.util.unaccent

/**
 * Displays the game screen.
 *
 * The game screen is composed of the following components :
 *
 * - The game's title
 * - Today's given routes
 * - Rows used to display [GuessResponse]s
 * - A searchable dropdown menu used to select a stop to send as guess
 * - A button that launches a guess or shares recap info at the end of the game.
 * - If map mode is on, a FAB that opens a map of Brussels with the guessed stop locations.
 *
 * While the ViewModel fetches initial data from the server, a loading screen is shown.
 * If it couldn't be fetched, an error message is displayed.
 */
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameScreenViewModel = viewModel(factory = STIBleViewModelProvider.Factory)
) {
    val requestState = viewModel.requestState
    var mapboxSheetShown by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier, floatingActionButton = {
        if (viewModel.isMapModeEnabled) {
            FloatingActionButton(onClick = {
                mapboxSheetShown = true
                viewModel.lockMapMode()
            }) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = stringResource(id = R.string.map_fab_content_description)
                )
            }
        }
    }) { innerPadding ->

        if (mapboxSheetShown) {
            MapModeBottomSheet(onDismissRequest = { mapboxSheetShown = false },
                stops = viewModel.madeGuesses.map { it.guessedStop })
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            STIBleTitle()

            if (!viewModel.isGameReady) {
                if (requestState is RequestState.Loading) {
                    LoadingScreen(Modifier.fillMaxSize())
                } else if (requestState is RequestState.Error) {
                    ErrorMessageScreen(
                        errorType = requestState.error,
                        onReload = viewModel::initializeGame,
                        Modifier.fillMaxSize()
                    )
                }
            } else {
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
                    isMapModeEnabled = viewModel.isMapModeEnabled,
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .padding(dimensionResource(id = R.dimen.outer_padding))
                )
            }
        }
    }

    if (requestState is RequestState.Error) {
        ShowToast(error = requestState.error)
    }
}

/**
 * A [MapWithStopsPoints] encased in a [ModalBottomSheet]
 */
@Composable
private fun MapModeBottomSheet(
    stops: List<Stop>, onDismissRequest: () -> Unit, modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        MapWithStopsPoints(stops = stops, modifier)
    }
}

/**
 * Display the app name with the correct style.
 */
@Composable
private fun STIBleTitle(modifier: Modifier = Modifier) {
    Row(modifier) {
        Text(
            text = stringResource(id = R.string.app_name_first_part),
            color = STIBleBlue,
            style = MaterialTheme.typography.displayLarge,
        )
        Text(
            text = stringResource(id = R.string.app_name_second_part),
            color = STIBleRed,
            style = MaterialTheme.typography.displayLarge,
        )
        Text(
            text = stringResource(id = R.string.app_name_third_part),
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

/**
 * Displays a little spinny thing.
 */
@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.outer_padding)))
        Text(text = stringResource(id = R.string.loading))
    }
}

/**
 * The actual body of the Game screen.
 */
@Composable
private fun GameScreenBody(
    gameRules: GameRules,
    userGuess: String,
    canStillPlay: Boolean,
    isMapModeEnabled: Boolean,
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
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.inner_padding)))
                RouteSquare(
                    gameRules.puzzleRoutes[it],
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.inner_padding)))

        /* GUESS ROWS */

        Column {
            repeat(gameRules.maxGuessCount) {
                GuessRow(guessResponse = guessHistory.getOrNull(it))
            }
        }

        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.inner_padding).times(2)))

        /* GUESS INPUT */

        StopSearchBar(
            userGuess, onUserGuessChange, gameRules.stops, canStillPlay, Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.inner_padding)))

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
                hasLost = gameState == GameState.LOST,
                easyMode = isMapModeEnabled
            )

            val shareHeader = "${stringResource(id = R.string.app_name)} #${gameRules.puzzleNumber}"

            ShareButton(
                shareMessage = shareText, shareHeader = shareHeader, Modifier.fillMaxWidth()
            )

        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.inner_padding)))

        /* POST GAME MESSAGE */

        if (mysteryStop != null) {
            val gameOverMsg =
                if (gameState == GameState.LOST) R.string.stop_name_lost else R.string.won

            Text(
                text = stringResource(id = gameOverMsg, mysteryStop),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
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
 * Button to share the game's recap
 */
@Composable
private fun ShareButton(shareMessage: String, shareHeader: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }
            context.startActivity(Intent.createChooser(intent, shareHeader))
        }, modifier = modifier, colors = ButtonDefaults.buttonColors(
            containerColor = STIBleGreen, contentColor = light_onSTIBleGreen
        )
    ) {
        Text(text = stringResource(id = R.string.share))
    }
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
 * @param easyMode true if the player enabled easy/map mode
 * @param puzzleNumber today's puzzle number
 * @param maxGuessCount the amount of times the user can guess in a day
 * @param guessHistory all [GuessResponse] received during the session
 * @param bestPercentage the proximity percentage of the guess closest to the mystery stop
 * @param hasLost whether the user has lost or not
 */
@Composable
private fun buildShareMessage(
    easyMode: Boolean,
    puzzleNumber: Int,
    maxGuessCount: Int,
    bestPercentage: Double,
    guessHistory: List<GuessResponse>,
    hasLost: Boolean
): String {
    // i've thought of not letting the user share if the game isn't finished, but who cares honestly.

    val nbTries = if (hasLost) "X" else guessHistory.size

    val squares = StringBuilder()

    guessHistory.forEach {
        squares.append(buildSquaresForShare(it)).append('\n')
    }

    return """
#${stringResource(id = R.string.app_name)} #${puzzleNumber} $nbTries/$maxGuessCount (${
        bestPercentage.times(
            100
        ).toInt()
    }%) ${if (easyMode) stringResource(id = R.string.share_easy_mode_on) else ""}

$squares

${stringResource(id = R.string.app_name)} App - https://stible.elitios.net/
"""
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
private fun StopSearchBar(
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
            allStops.filter { it.unaccent().contains(userGuess.unaccent(), ignoreCase = true) }
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

@Preview
@Composable
private fun GameScreenBodyPreview() {
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
        isMapModeEnabled = true,
        modifier = Modifier.fillMaxSize()
    )
}