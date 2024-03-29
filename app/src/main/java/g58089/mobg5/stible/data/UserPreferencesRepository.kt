package g58089.mobg5.stible.data

import g58089.mobg5.stible.data.dto.UserPreferences
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
    suspend fun setMaxGuessCount(maxGuessCount: Int)
    suspend fun setMapModeLockPuzzleNumber(mapModeLockPuzzleNumber: Int)

    /**
     * Restores all user data and preferences to their defaults.
     */
    suspend fun clearPreferences()
}