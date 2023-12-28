package g58089.mobg5.stible.model

import android.content.Context
import g58089.mobg5.stible.model.database.OfflineCurrentSessionRepository
import g58089.mobg5.stible.model.database.OfflineGameHistoryRepository
import g58089.mobg5.stible.model.database.STIBleDatabase
import g58089.mobg5.stible.model.network.OnlineGameInteraction
import g58089.mobg5.stible.model.network.STIBleApi


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val gameHistoryRepository: GameHistoryRepository
    val currentSessionRepository: CurrentSessionRepository
    val gameInteraction: GameInteraction
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

    /**
     * Implementation for [CurrentSessionRepository]
     */
    override val currentSessionRepository: CurrentSessionRepository by lazy {
        OfflineCurrentSessionRepository(STIBleDatabase.getDatabase(context).currentSessionDao())
    }

    /**
     * Implementation for [GameInteraction]
     */
    override val gameInteraction: GameInteraction by lazy {
        OnlineGameInteraction(STIBleApi.retrofitService)
    }
}
