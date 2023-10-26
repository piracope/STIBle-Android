package g58089.mobg5.remise1

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import g58089.mobg5.remise1.ui.LoginScreen

enum class Remise1Screen() {
    Login,
    Logo
}

@Composable
fun Remise1App(
    navController: NavHostController = rememberNavController()
) {
    Surface {
        LoginScreen()
    }
}