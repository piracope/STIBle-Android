package g58089.mobg5.stible

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import g58089.mobg5.stible.ui.navigation.STIBleNavHost
import g58089.mobg5.stible.ui.navigation.STIBleNavigationBar
import g58089.mobg5.stible.ui.navigation.STIBleScreen

/**
 * Top composable of the application.
 *
 * Displays a navigation bottom bar and sets up the navigation host.
 */
@Composable
fun STIBleApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        bottomBar = {
            val bottomNavigableScreens =
                listOf(
                    STIBleScreen.Main,
                    STIBleScreen.Stats,
                    STIBleScreen.Settings,
                    STIBleScreen.Help
                )
            STIBleNavigationBar(navItems = bottomNavigableScreens, navController = navController)
        }
    ) { innerPadding ->
        Surface(modifier = modifier.padding(innerPadding)) {
            STIBleNavHost(
                navController,
                modifier
            )
        }
    }
}