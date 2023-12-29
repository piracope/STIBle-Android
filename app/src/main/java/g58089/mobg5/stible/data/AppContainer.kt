package g58089.mobg5.stible.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import g58089.mobg5.stible.data.database.OfflineCurrentSessionRepository
import g58089.mobg5.stible.data.database.OfflineGameHistoryRepository
import g58089.mobg5.stible.data.database.STIBleDatabase
import g58089.mobg5.stible.data.network.OnlineGameInteraction
import g58089.mobg5.stible.data.network.STIBleApi
import g58089.mobg5.stible.data.preferences.OfflineUserPreferencesRepository

/**
 * Name of the DataStore where our user preferences will be stored.
 */
private const val USER_PREFERENCES_NAME = "user_preferences"

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val gameHistoryRepository: GameHistoryRepository
    val currentSessionRepository: CurrentSessionRepository
    val userPreferencesRepository: UserPreferencesRepository
    val gameInteraction: GameInteraction
}

/**
 * [AppContainer] implementation that uses a local database for history saving.
 *
 * Provides instance of [OfflineGameHistoryRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Creates a DataStore for the provided context.
     */
    private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

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
     * Implementation for [UserPreferencesRepository]
     */
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        OfflineUserPreferencesRepository(context.dataStore)
    }

    /**
     * Implementation for [GameInteraction]
     */
    override val gameInteraction: GameInteraction by lazy {
        OnlineGameInteraction(STIBleApi.retrofitService)
    }
}
