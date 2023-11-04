package g58089.mobg5.remise1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import g58089.mobg5.remise1.ui.theme.Remise1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Remise1Theme {
                Remise1App()
            }
        }
    }
}