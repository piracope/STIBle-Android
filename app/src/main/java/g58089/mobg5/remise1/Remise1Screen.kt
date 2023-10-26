package g58089.mobg5.remise1

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import g58089.mobg5.remise1.ui.LoginScreen
import g58089.mobg5.remise1.ui.LogoScreen

enum class Remise1Screen() {
    Login,
    Logo
}

@Composable
fun Remise1App(
    navController: NavHostController = rememberNavController()
) {
    Surface {
        NavHost(navController = navController, startDestination = Remise1Screen.Login.name) {
            composable(route = Remise1Screen.Login.name) {
                LoginScreen(
                    onLoginConfirmed = {
                        navController.navigate(Remise1Screen.Logo.name)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = Remise1Screen.Logo.name) {
                LogoScreen(modifier = Modifier.fillMaxHeight())
            }
        }
    }
}