package g58089.mobg5.stible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import g58089.mobg5.stible.ui.theme.STIBleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            STIBleTheme {
                STIBleApp()
            }
        }
    }
}