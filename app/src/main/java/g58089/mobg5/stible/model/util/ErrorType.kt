package g58089.mobg5.stible.model.util

/**
 * Defines general error cases.
 */
enum class ErrorType {
    /**
     * Couldn't reach the backend server.
     */
    NO_INTERNET,

    /**
     * The game rules the user is playing with are outdated.
     */
    NEW_LEVEL_AVAILABLE,

    /**
     * For some reason, the user language is not recognized.
     */
    BAD_LANGUAGE,

    /**
     * The stop provided by the user does not exist.
     */
    BAD_STOP,

    /**
     * The game is already over. Why bother guessing ?
     */
    GAME_OVER,

    /**
     * Catch-all lol
     */
    UNKNOWN
}