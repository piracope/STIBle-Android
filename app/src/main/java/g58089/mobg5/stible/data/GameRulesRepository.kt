package g58089.mobg5.stible.data

import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.util.Language

interface GameRulesRepository {

    /**
     * Retrieves [GameRules] from the game's backend according to the player's [Language]
     */
    suspend fun getGameRules(lang: Language): GameRules
}