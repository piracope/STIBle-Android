package g58089.mobg5.stible

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import g58089.mobg5.stible.ui.GameScreen
import g58089.mobg5.stible.ui.HelpScreen
import g58089.mobg5.stible.ui.MainScreenViewModel

/**
 * The different Navigation routes leading to different Screens.
 */
enum class NavRoutes {
    /**
     * The Main Screen Navigation route.
     */
    Main,

    /**
     * The Help Screen Navigation route.
     */
    Help,

    /**
     * The Stats Screen Navigation route.
     */
    Stats,


    /**
     * The Settings Screen Navigation route
     */
    Settings,


    /**
     * The Maps bottom app sheet.
     *
     * NOTE: idk if it works with navigate
     */
    Maps,
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
            val currentRoute = navController.currentBackStackEntry?.destination?.route

            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == NavRoutes.Main.name,
                    onClick = {
                        navController.navigate(NavRoutes.Main.name)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = stringResource(
                                id = R.string.home_content_desc
                            )
                        )
                    }
                )
                NavigationBarItem(
                    selected = currentRoute == NavRoutes.Stats.name,
                    onClick = {
                        navController.navigate(NavRoutes.Stats.name)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = stringResource(
                                id = R.string.stats_content_desc
                            )
                        )
                    }
                )
                NavigationBarItem(
                    selected = currentRoute == NavRoutes.Settings.name,
                    onClick = {
                        navController.navigate(NavRoutes.Settings.name)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(
                                id = R.string.settings_content_desc
                            )
                        )
                    }
                )
                NavigationBarItem(
                    selected = currentRoute == NavRoutes.Help.name,
                    onClick = {
                        navController.navigate(NavRoutes.Help.name)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Help,
                            contentDescription = stringResource(
                                id = R.string.help_content_desc
                            )
                        )
                    }
                )
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
    Surface(modifier = modifier) {
        // initial route is always Login. Will implement stay logged in functionality if required.
        NavHost(navController = navController, startDestination = NavRoutes.Main.name) {

            composable(route = NavRoutes.Main.name) {
                GameScreen(
                    gameRules = viewModel.gameRules,
                    userGuess = viewModel.userGuess,
                    onUserGuessChange = { viewModel.guessChange(it) },
                    onGuess = { viewModel.guess() },
                    requestState = viewModel.requestState,
                    canGuess = viewModel.canGuess,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            dimensionResource(R.dimen.main_padding)
                        )
                )
            }

            composable(route = NavRoutes.Stats.name) {
                // TODO: Stats Screen
            }

            composable(route = NavRoutes.Settings.name) {
                // TODO: Settings Screen
            }

            composable(route = NavRoutes.Help.name) {
                HelpScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}