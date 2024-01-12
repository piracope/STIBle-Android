package g58089.mobg5.stible.data.network

import g58089.mobg5.stible.data.GameRulesRepository
import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.util.ErrorType
import g58089.mobg5.stible.data.util.Language
import g58089.mobg5.stible.data.util.STIBleException
import retrofit2.HttpException
import java.io.IOException

class OnlineGameRulesRepository(private val stibleApi: STIBleApiService) : GameRulesRepository {
    private var rules: GameRules? = null

    override suspend fun getGameRules(lang: Language): GameRules {
        return rules ?: try {
            stibleApi.start(lang.code).also { rules = it }
        } catch (e: IOException) {
            throw STIBleException(ErrorType.NO_INTERNET)
        } catch (e: HttpException) {
            if (e.code() == 400)
            // language wasn't "fr" or "nl" --> should never happen
                throw STIBleException(ErrorType.BAD_LANGUAGE)
            else
            // idk an error 500 or something you can never be sure
                throw STIBleException(ErrorType.UNKNOWN)
        }
    }
}