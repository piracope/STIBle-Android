package g58089.mobg5.stible.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import g58089.mobg5.stible.R
import g58089.mobg5.stible.ui.screens.GameScreen
import g58089.mobg5.stible.ui.screens.HelpScreen
import g58089.mobg5.stible.ui.screens.SettingsScreen

/**
 * The different navigation destinations.
 */
sealed class Screen(
    /**
     * Unique name to define the destination screen.
     */
    val route: String,

    /**
     * String resource ID containing the title of this screen.
     */
    @StringRes val nameResId: Int,

    /**
     * Icon that represents this screen.
     */
    val icon: ImageVector
) {
    data object Main : Screen("main", R.string.home, Icons.Default.Home)
    data object Help : Screen("help", R.string.help, Icons.Default.Help)
    data object Stats : Screen("stats", R.string.stats, Icons.Default.BarChart)
    data object Settings : Screen("settings", R.string.settings, Icons.Default.Settings)
}

/**
 * [NavigationBar] that displays buttons to access the provided [Screen]s.
 */
@Composable
fun STIBleNavigationBar(
    navItems: List<Screen>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val currentNav by navController.currentBackStackEntryAsState()
    val currentRoute = currentNav?.destination

    NavigationBar(modifier = modifier) {
        navItems.forEach { screen ->
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


/**
 * Navigation host for the application.
 *
 * Defines the navigation graph of the application.
 */
@Composable
fun STIBleNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = modifier
    ) {
        composable(route = Screen.Main.route) {
            GameScreen()
        }

        composable(route = Screen.Stats.route) {
            // TODO: Stats Screen
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen()
        }

        composable(route = Screen.Help.route) {
            HelpScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.outer_padding))
            )
        }
    }
}
