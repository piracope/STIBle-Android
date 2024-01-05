package g58089.mobg5.stible.data.dto

/**
 * Data needed to be stored in as key-value pairs.
 */
data class UserPreferences(
    /**
     * The puzzle number of the last game session.
     */
    val lastSeenPuzzleNumber: Int = -1,

    /**
     * Whether the user may access a map showing them where their guesses are.
     */
    val isMapModeEnabled: Boolean = false,

    /**
     * The number of guesses a player has for each puzzle.
     */
    val maxGuessCount: Int = 6
)

