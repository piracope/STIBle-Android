package g58089.mobg5.stible

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import g58089.mobg5.stible.ui.AboutScreen
import g58089.mobg5.stible.ui.LoginScreen
import g58089.mobg5.stible.ui.LoginState
import g58089.mobg5.stible.ui.LogoScreen
import g58089.mobg5.stible.ui.STIBleViewModel

/**
 * The different Navigation routes leading to different Screens.
 */
enum class NavRoutes {
    /**
     * The Login Screen Navigation route.
     */
    Login,

    /**
     * The Logo Screen Navigation route.
     */
    Logo,

    /**
     * The About Screen Navigation route.
     */
    About
}

/**
 * Main composable of the application.
 *
 * Controls the navigation behaviour.
 */
@Composable
fun STIBleApp(
    viewModel: STIBleViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            if (viewModel.loginState is LoginState.Success) {
                val currentRoute = navController.currentBackStackEntry?.destination?.route

                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == NavRoutes.Logo.name,
                        onClick = {
                            navController.navigate(NavRoutes.Logo.name)
                        },
                        //FIXME: content description null ?
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
                        selected = currentRoute == NavRoutes.About.name,
                        onClick = {
                            navController.navigate(NavRoutes.About.name)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = stringResource(
                                    id = R.string.about_content_desc
                                )
                            )
                        }
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
    viewModel: STIBleViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        val loginState = viewModel.loginState

        // initial route is always Login. Will implement stay logged in functionality if required.
        NavHost(navController = navController, startDestination = NavRoutes.Login.name) {

            // Logo Screen route, should be displayed after successful login
            composable(route = NavRoutes.Logo.name) {
                LogoScreen(modifier = Modifier.fillMaxSize())
            }

            // Login Screen route, should be displayed upon opening the app
            composable(route = NavRoutes.Login.name) {
                LoginScreen(
                    email = viewModel.userEmail,
                    password = viewModel.userPassword,
                    loginState = loginState,
                    onEmailChange = {
                        viewModel.updateUserEmail(it)
                    },
                    onPasswordChange = {
                        viewModel.updateUserPassword(it)
                    },
                    onLoginAttempt = {
                        viewModel.checkUserEmail()
                    },
                    onNavigateLoginSuccess = {
                        navController.navigate(NavRoutes.Logo.name) {
                            popUpTo(0) // we navigate and remove everything before us
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = NavRoutes.About.name) {
                AboutScreen(Modifier.fillMaxSize())
            }
        }
    }
}