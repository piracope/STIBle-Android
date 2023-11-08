package g58089.mobg5.remise1

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import g58089.mobg5.remise1.ui.LoginScreen
import g58089.mobg5.remise1.ui.LogoScreen
import g58089.mobg5.remise1.ui.Remise1ViewModel

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
    Logo
}

/**
 * Main composable of the application.
 *
 * Controls the navigation behaviour.
 */
@Composable
fun Remise1App(
    viewModel: Remise1ViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Surface {
        val uiState = viewModel.uiState

        // initial route is always Login. Will implement stay logged in functionality if required.
        NavHost(navController = navController, startDestination = NavRoutes.Login.name) {

            // Logo Screen route, should be displayed after successful login
            composable(route = NavRoutes.Logo.name) {
                LogoScreen(modifier = Modifier.fillMaxHeight())
            }

            // Login Screen route, should be displayed upon opening the app
            composable(route = NavRoutes.Login.name) {
                LoginScreen(
                    email = viewModel.userEmail,
                    isEmailWrong = uiState.isEmailWrong,
                    isLoginSuccessful = uiState.isLoginSuccessful,
                    onEmailChange = {
                        viewModel.updateUserEmail(it)
                    },
                    onLoginAttempt = {
                        viewModel.checkUserEmail()
                    },
                    onNavigateLoginSuccess = {
                        navController.navigate(NavRoutes.Logo.name) {
                            popUpTo(0) // we navigate and remove everything before us
                        }
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

        }
    }
}