package g58089.mobg5.stible

import android.app.Application
import g58089.mobg5.stible.model.AppContainer
import g58089.mobg5.stible.model.AppDataContainer


/**
 * Probably a class created and launched at the start of the application. Probably.
 * I'm just taking this straight from that one Room Google codelab.
 *
 * This is probably over-engineering.
 */
class STIBleApplication : Application() {

    /**
     * [AppContainer] instance used by the rest of classes to obtain dependencies.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}