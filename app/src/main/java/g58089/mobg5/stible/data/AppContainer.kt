package g58089.mobg5.stible.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import g58089.mobg5.stible.data.database.OfflineCurrentSessionRepository
import g58089.mobg5.stible.data.database.OfflineGameHistoryRepository
import g58089.mobg5.stible.data.database.STIBleDatabase
import g58089.mobg5.stible.data.datastore.OfflineUserPreferencesRepository
import g58089.mobg5.stible.data.locale.AndroidLocaleRepository
import g58089.mobg5.stible.data.network.OnlineGameInteraction
import g58089.mobg5.stible.data.network.OnlineGameRulesRepository
import g58089.mobg5.stible.data.network.STIBleApi

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val gameHistoryRepository: GameHistoryRepository
    val currentSessionRepository: CurrentSessionRepository
    val userPreferencesRepository: UserPreferencesRepository
    val localeRepository: LocaleRepository
    val gameRulesRepository: GameRulesRepository
    val gameInteraction: GameInteraction
}

/**
 * [AppContainer] implementation that uses a local database for history saving
 * and interacts with an online backend.
 *
 * Provides instance of
 * - [OfflineGameHistoryRepository]
 * - [OfflineCurrentSessionRepository]
 * - [OfflineUserPreferencesRepository]
 * - [AndroidLocaleRepository]
 * - [OnlineGameInteraction]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Creates a DataStore for the provided [Context].
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
     * Implementation for [LocaleRepository].
     */
    override val localeRepository: LocaleRepository by lazy {
        AndroidLocaleRepository(context)
    }

    /**
     * Implementation for [GameInteraction]
     */
    override val gameInteraction: GameInteraction by lazy {
        OnlineGameInteraction(STIBleApi.retrofitService)
    }

    override val gameRulesRepository: GameRulesRepository by lazy {
        OnlineGameRulesRepository(STIBleApi.retrofitService)
    }

    companion object {
        /**
         * Name of the DataStore where our user preferences will be stored.
         */
        private const val USER_PREFERENCES_NAME = "user_preferences"
    }
}
