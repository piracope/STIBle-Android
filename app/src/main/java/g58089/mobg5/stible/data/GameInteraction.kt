package g58089.mobg5.stible.data

import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.util.Language

/**
 * Façade to interact with the game, like making a guess.
 */
interface GameInteraction {


    /**
     * Makes a guess and sends it to the game's backend
     */
    suspend fun guess(
        stopName: String,
        puzzleNumber: Int,
        tryNumber: Int,
        lang: Language
    ): GuessResponse

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