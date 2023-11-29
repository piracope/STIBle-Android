package g58089.mobg5.stible.model

/**
 * All data essential to the working of the game.
 */
data class GameRules(

    /**
     * All routes passing on the mystery Stop.
     */
    val puzzleRoutes: List<Route>,

    /**
     * The list of all possible stops that can act as a guess.
     *
     * All valid guesses.
     */
    val stops: List<String>,

    /**
     * The maximum amount of time the user can guess in a day.
     */
    val maxGuessCount: Int,

    /**
     * The puzzle number for which this data is valid.
     *
     * The puzzle number is incremented daily (usually) server-side.
     * All guesses where the puzzleNumber differs from the server's
     * will be discarded.
     */
    val puzzleNumber: Int
)
