package g58089.mobg5.stible.model.database

import android.content.Context
import g58089.mobg5.stible.model.GameHistoryRepository


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val gameHistoryRepository: GameHistoryRepository
}

/**
 * [AppContainer] implementation that uses a local database for history saving.
 *
 * Provides instance of [OfflineGameHistoryRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [GameHistoryRepository]
     */
    override val gameHistoryRepository: GameHistoryRepository by lazy {
        OfflineGameHistoryRepository(STIBleDatabase.getDatabase(context).gameHistoryDao())
    }
}
