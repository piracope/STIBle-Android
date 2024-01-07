package g58089.mobg5.stible.data

import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.util.Language

/**
 * Façade to interact with the game, such as getting [GameRules] or making a guess.
 */
interface GameInteraction {

    /**
     * Retrieves [GameRules] from the game's backend according to the player's [Language]
     */
    suspend fun getGameRules(lang: Language): GameRules

    /**
     * Makes a guess and sends it to the game's backend
     */
    suspend fun guess(
        stopName: String,
        puzzleNumber: Int,
        tryNumber: Int,
        lang: Language
    ): GuessResponse
    /* FIXME: this has nothing to do in an interface
     *
     * Maybe, here in GameInteraction, only assume that implementations will return a
     * GuessResponse, and then let the implementation handle errors.
     * tbh, we only NEED to have a Response because I need to handle code 205, but if the
     * network layer handles it, throws an exception, then the application layer can just
     * use the exception instead of validating code 205 and 400 itself.
     */

    /**
     * Translates a Stop's name from one [Language] to another.
     *
     * For example, translating "Cimetière d'Ixelles" from [Language.FRENCH] to [Language.DUTCH]
     * returns "Begraafplaats van Elsene".
     *
     * If the translation fails, for example translating a French stop name to French, the
     * given stopName will be returned.
     */
    suspend fun translateStop(stopName: String, oldLang: Language, newLang: Language): String
}