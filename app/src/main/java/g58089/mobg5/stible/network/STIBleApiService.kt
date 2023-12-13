package g58089.mobg5.stible.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import g58089.mobg5.stible.model.dto.GameRules
import g58089.mobg5.stible.model.dto.Guess
import g58089.mobg5.stible.model.dto.GuessResponse
import g58089.mobg5.stible.model.util.Language
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Base URL of the login server.
 */
private const val BASE_URL = "https://stible.elitios.net"

/**
 * JSON Serializer.
 */
private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    //NOTE: isLenient => SOME route colors are passed as Int.... for some reason
}

/**
 * HTTP request engine.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Manages all request directed to the remote server.
 */
interface STIBleApiService {

    /**
     * Sends a game start request.
     *
     * Upon success, returns all necessary data to run the game,
     * including all possible stop names in the language passed
     * as parameter
     *
     * @param lang the user's language, must be "fr" (default) or "nl"
     */
    @POST("start")
    suspend fun start(@Body lang: String = Language.FRENCH.code): GameRules

    @POST("guess")
    suspend fun guess(@Body guess: Guess) : Response<GuessResponse>
}

/**
 * Publicly accessible façade for making calls to the remote API.
 */
object STIBleApi {
    /**
     * HTTP call handler.
     */
    val retrofitService: STIBleApiService by lazy {
        retrofit.create(STIBleApiService::class.java)
    }
}