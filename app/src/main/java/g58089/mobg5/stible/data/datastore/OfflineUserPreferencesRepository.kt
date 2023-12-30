package g58089.mobg5.stible.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import g58089.mobg5.stible.data.UserPreferencesRepository
import g58089.mobg5.stible.data.database.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val TAG = "OfflineUserPreferencesRepository"

class OfflineUserPreferencesRepository(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepository {

    private object PreferencesKeys {
        val LAST_SEEN_PUZZLE_NUMBER = intPreferencesKey("last_seen_puzzle_number")
        val IS_MAP_MODE_ENABLED = booleanPreferencesKey("is_map_mode_enabled")
    }

    override val userData: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "IOException during DataStore read.")
                emit(emptyPreferences())
            } else {
                // rethrow because this is bad
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    override suspend fun setLastSeenPuzzleNumber(puzzleNumber: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_SEEN_PUZZLE_NUMBER] = puzzleNumber
        }
    }

    override suspend fun setIsMapModeEnabled(isMapModeEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_MAP_MODE_ENABLED] = isMapModeEnabled
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val lastSeenPuzzleNumber = preferences[PreferencesKeys.LAST_SEEN_PUZZLE_NUMBER] ?: -1
        val isMapModeEnabled = preferences[PreferencesKeys.IS_MAP_MODE_ENABLED] ?: false
        return UserPreferences(lastSeenPuzzleNumber, isMapModeEnabled)
    }


}