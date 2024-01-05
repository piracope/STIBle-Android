package g58089.mobg5.stible.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import g58089.mobg5.stible.R

/**
 * The different navigation destinations.
 */
sealed class STIBleScreen(
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
    data object Main : STIBleScreen("main", R.string.home, Icons.Default.Home)
    data object Help : STIBleScreen("help", R.string.help, Icons.Default.Help)
    data object Stats : STIBleScreen("stats", R.string.stats, Icons.Default.BarChart)
    data object Settings : STIBleScreen("settings", R.string.settings, Icons.Default.Settings)
}