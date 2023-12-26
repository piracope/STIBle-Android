package g58089.mobg5.stible.model.util

/**
 * The different states of play this session can be in.
 */
enum class GameState {
    /**
     * The user can make guesses.
     */
    PLAYING,

    /**
     * The user ran out of possible guesses.
     *
     * The secret should be revealed.
     */
    LOST,

    /**
     * The user found the secret stop in time.
     */
    WON,

    /**
     * The user is blocked form input.
     *
     * This can be because a guess is pending, we couldn't fetch initial data, ...
     */
    BLOCKED
}