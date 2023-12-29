package g58089.mobg5.stible.data.preferences

/**
 * Data needed to be stored in as key-value pairs.
 */
data class UserPreferences(
    /**
     * The puzzle number of the last game session.
     */
    val lastSeenPuzzleNumber: Int,

    /**
     * Whether the user may access a map showing them where their guesses are.
     */
    val isMapModeEnabled: Boolean
)
