package g58089.mobg5.stible.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import g58089.mobg5.stible.ui.screens.GameScreen
import g58089.mobg5.stible.ui.screens.HelpScreen
import g58089.mobg5.stible.ui.screens.SettingsScreen

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
            HelpScreen()
        }
    }
}
