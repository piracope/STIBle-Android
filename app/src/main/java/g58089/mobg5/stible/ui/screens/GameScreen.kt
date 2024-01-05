package g58089.mobg5.stible.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.bindgen.Value
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import g58089.mobg5.stible.R
import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.dto.Route
import g58089.mobg5.stible.data.dto.Stop
import g58089.mobg5.stible.data.network.RequestState
import g58089.mobg5.stible.data.util.ErrorType
import g58089.mobg5.stible.data.util.GameState
import g58089.mobg5.stible.ui.STIBleViewModelProvider
import g58089.mobg5.stible.ui.theme.STIBleBlue
import g58089.mobg5.stible.ui.theme.STIBleGreen
import g58089.mobg5.stible.ui.theme.STIBleRed
import g58089.mobg5.stible.ui.theme.STIBleYellow
import g58089.mobg5.stible.ui.theme.light_onSTIBleGreen
import g58089.mobg5.stible.ui.util.ShowToast
import g58089.mobg5.stible.ui.util.getErrorStringResId
import g58089.mobg5.stible.ui.util.unaccent
import java.util.Locale

private const val TAG = "GameScreen"

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameScreenViewModel = viewModel(factory = STIBleViewModelProvider.Factory)
) {
    val requestState = viewModel.requestState
    var mapboxSheetShown by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier, floatingActionButton = {
            if (viewModel.isMapModeEnabled) {
                FloatingActionButton(onClick = { mapboxSheetShown = true }) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = stringResource(id = R.string.map_fab_content_description)
                    )
                }
            }
        }
    ) { innerPadding ->

        if (mapboxSheetShown) {
            MapboxBottomSheet(
                onDismissRequest = { mapboxSheetShown = false },
                stops = viewModel.madeGuesses.map { Stop(it.stopName) })
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

@OptIn(MapboxExperimental::class)
@Composable
fun MapWithStopsPoints(stops: List<Stop>, modifier: Modifier = Modifier) {
    val initialZoom = 11.5
    val initialPitch = 0.0
    val centerPoint = Point.fromLngLat(4.34878, 50.85045) // Brussels center
    val minZoom = 10.5
    val brusselsCoordinateBounds = CoordinateBounds(
        Point.fromLngLat(4.26, 50.77),
        Point.fromLngLat(4.52, 50.93),
        false
    )
    val markerImage = ImageBitmap.imageResource(R.drawable.red_marker).asAndroidBitmap()
    val textIconOffset = listOf(0.0, -2.0)
    val textIconSize = MaterialTheme.typography.labelLarge.fontSize.value.toDouble()
    val textIconColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val textIconHalo = MaterialTheme.colorScheme.surfaceVariant.toArgb()
    val textIconHaloStrength = 1.0

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(initialZoom)
            pitch(initialPitch)
            center(centerPoint)
        }
    }

    MapboxMap(
        modifier,
        mapViewportState = mapViewportState,
    ) {

        val darkTheme = isSystemInDarkTheme()

        MapEffect(Unit) { mapView ->
            val cameraBoundsOptions = CameraBoundsOptions.Builder()
                .bounds(brusselsCoordinateBounds)
                .minZoom(minZoom)
                .build()

            val mapboxMap = mapView.mapboxMap

            mapboxMap.setBounds(cameraBoundsOptions)
            mapboxMap.loadStyle(if (darkTheme) Style.DARK else Style.STANDARD) { style ->
                style.setStyleImportConfigProperty(
                    "basemap",
                    "showTransitLabels",
                    Value.valueOf(false)
                )
            }
        }



        MapEffect(stops) { mapView ->
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()

            stops.forEach { stop ->
                val point = PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(stop.longitude, stop.latitude))
                    .withIconImage(markerImage)
                    .withIconAnchor(IconAnchor.BOTTOM)
                    .withTextField(stop.name)
                    .withTextAnchor(TextAnchor.BOTTOM)
                    .withTextOffset(textIconOffset)
                    .withTextSize(textIconSize)
                    .withTextColor(textIconColor)
                    .withTextHaloColor(textIconHalo)
                    .withTextHaloWidth(textIconHaloStrength)
                pointAnnotationManager.create(point)
            }
        }
    }
}

@Composable
fun MapboxBottomSheet(
    stops: List<Stop>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        MapWithStopsPoints(stops = stops, modifier)
    }

}

@Composable
fun STIBleTitle(modifier: Modifier = Modifier) {
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
fun LoadingScreen(modifier: Modifier = Modifier) {
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
 * Displays an error message according to a provided [ErrorType] and a reload button.
 */
@Composable
fun ErrorMessageScreen(errorType: ErrorType, onReload: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var icon = Icons.Default.Error

        if (errorType == ErrorType.NO_INTERNET) {
            icon = Icons.Default.WifiOff
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(id = R.dimen.error_icon))
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.outer_padding)))
        Text(text = stringResource(id = getErrorStringResId(errorType)))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.outer_padding)))
        Button(onClick = onReload) {
            Text(text = stringResource(id = R.string.reload))
        }
    }
}

@Composable
fun GameScreenBody(
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = STIBleGreen,
                    contentColor = light_onSTIBleGreen
                )
            ) {
                Text(text = stringResource(id = R.string.share))
            }
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
            modifier = Modifier.weight(1f)
        )

        val colorDirection = getDirectionBackgroundColor(guessResponse)
        val vector = guessResponse?.directionEmoji?.let { emojiToIcon(it) }
        GuessRowIcon(
            icon = vector,
            bgColor = colorDirection,
            fgColor = if (colorDirection == STIBleGreen) light_onSTIBleGreen else MaterialTheme.colorScheme.onPrimaryContainer,
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
    val color =
        if (text == null) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.secondary
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
            .background(color)
            .height(dimensionResource(R.dimen.guess_row_height))

    ) {
        text?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
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
            val color: Color = if (sqNb < nbOfGreen) STIBleGreen
            else if (sqNb < nbOfGreen + nbOfYellow) STIBleYellow
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
private fun GuessRowIcon(
    icon: ImageVector?,
    bgColor: Color,
    fgColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_amount)))
            .background(bgColor)
    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = it.name, tint = fgColor)
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
        return MaterialTheme.colorScheme.surfaceVariant
    }

    if (guess.directionEmoji == "✅") { // doesn't display on my machine but it's
        return STIBleGreen
    }

    return MaterialTheme.colorScheme.primaryContainer
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
        isMapModeEnabled = true,
        modifier = Modifier.fillMaxSize()
    )
}