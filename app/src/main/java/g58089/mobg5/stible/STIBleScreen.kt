package g58089.mobg5.stible

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import g58089.mobg5.stible.model.util.ErrorType
import g58089.mobg5.stible.network.RequestState
import g58089.mobg5.stible.ui.GameScreen
import g58089.mobg5.stible.ui.HelpScreen
import g58089.mobg5.stible.ui.MainScreenViewModel

/**
 * The different Screens and their navigation route.
 */
sealed class Screen(val route: String, @StringRes val nameResId: Int, val icon: ImageVector) {
    object Main : Screen("main", R.string.home, Icons.Default.Home)
    object Help : Screen("help", R.string.help, Icons.Default.Help)
    object Stats : Screen("stats", R.string.stats, Icons.Default.BarChart)
    object Settings : Screen("settings", R.string.settings, Icons.Default.Settings)
}

/**
 * Main composable of the application.
 *
 * Controls the navigation behaviour.
 */
@Composable
fun STIBleApp(
    viewModel: MainScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            val bottomNavigableScreens =
                listOf(Screen.Main, Screen.Stats, Screen.Settings, Screen.Help)
            val currentNav by navController.currentBackStackEntryAsState()
            val currentRoute = currentNav?.destination

            NavigationBar {

                bottomNavigableScreens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute?.route == screen.route,
                        onClick = { navController.navigate(screen.route) },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = stringResource(
                                    id = screen.nameResId
                                )
                            )
                        },
                        label = { Text(text = stringResource(id = screen.nameResId)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        STIBleScreenContent(viewModel, navController, Modifier.padding(innerPadding))
    }
}

@Composable
fun STIBleScreenContent(
    viewModel: MainScreenViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    // TODO: have a splash screen that is open while initial data isn't fetched.
    // or have a spinning thing, you know those ones

    Surface(modifier = modifier) {
        NavHost(navController = navController, startDestination = Screen.Main.route) {

            composable(route = Screen.Main.route) {
                GameScreen(
                    // FIXME: since the screen is so dependent on the viewmodel, why not pass it
                    gameRules = viewModel.gameRules,
                    userGuess = viewModel.userGuess,
                    onUserGuessChange = { viewModel.guessChange(it) },
                    onGuess = { viewModel.guess() },
                    gameState = viewModel.gameState,
                    canStillPlay = viewModel.canGuess,
                    guessHistory = viewModel.madeGuesses,
                    mysteryStop = viewModel.mysteryStop,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            dimensionResource(R.dimen.main_padding)
                        )
                )
            }

            composable(route = Screen.Stats.route) {
                // TODO: Stats Screen
            }

            composable(route = Screen.Settings.route) {
                // TODO: Settings Screen
            }

            composable(route = Screen.Help.route) {
                HelpScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.main_padding))
                )
            }
        }
    }

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