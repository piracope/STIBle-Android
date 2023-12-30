package g58089.mobg5.stible

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import g58089.mobg5.stible.ui.theme.STIBleTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            STIBleTheme {
                STIBleApp()
            }
        }
    }
}