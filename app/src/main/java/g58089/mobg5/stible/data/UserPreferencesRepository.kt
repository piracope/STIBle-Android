package g58089.mobg5.stible.data

import g58089.mobg5.stible.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides retrieval and update of [UserPreferences].
 */
interface UserPreferencesRepository {

    /**
     * The [UserPreferences] stored in the data source.
     */
    val userData: Flow<UserPreferences>

    suspend fun setLastSeenPuzzleNumber(puzzleNumber: Int)
    suspend fun setIsMapModeEnabled(isMapModeEnabled: Boolean)
}