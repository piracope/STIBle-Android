package g58089.mobg5.stible.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.dto.Guess
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.dto.StopTranslation
import g58089.mobg5.stible.data.util.Language
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
private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL).build()

/**
 * Manages all requests directed to the remote server.
 */
interface STIBleApiService {

    /**
     * Sends a game start request.
     *
     * Upon success, returns this day's [GameRules], the initial data necessary to run the game.
     * This data includes all possible stop names in the language passed as a parameter.
     *
     * @param lang the user's language, must be "fr" (default) or "nl"
     */
    @POST("start")
    suspend fun start(@Body lang: String = Language.FRENCH.code): GameRules

    /**
     * Sends a [Guess] for today's mystery stop.
     *
     * Can return :
     * - 200 with a [GuessResponse]
     * - 205 : this guess is outdated (guess made at 00:01 when the server has already chosen a new
     *          puzzle
     * - 400 : the guess' stop doesn't exist
     * - 400 : a server exception occurred (yes an error 400 is stupid)
     */
    @POST("guess")
    suspend fun guess(@Body guess: Guess): Response<GuessResponse>

    /**
     * Sends a [StopTranslation] to the backend.
     *
     * Can return :
     * - 200 : a [String], the translated stop name
     * - 204 : the [StopTranslation.stopName] was blank
     * - 400 : a server exception occurred
     */
    @POST("tl")
    suspend fun translate(@Body translation: StopTranslation): String
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