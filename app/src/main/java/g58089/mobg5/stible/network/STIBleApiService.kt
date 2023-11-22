package g58089.mobg5.stible.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import g58089.mobg5.stible.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Base URL of the login server.
 */
private const val BASE_URL = "https://dnsrivnxleeqdtbyhftv.supabase.co"

/**
 * API Key used to authorize each request.
 */
private const val API_KEY = BuildConfig.API_KEY

/**
 * JSON Serializer.
 */
private val json = Json {
    ignoreUnknownKeys = true
}

/**
 * HTTP request engine.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Manages all request directed to the remote server.
 */
interface STIBleApiService {

    /**
     * Sends an authentication request.
     *
     * Upon success, returns an AuthUser containing the token of the authenticated user.
     * Upon failure, returns an HTTP error code, which generates an HttpException.
     */
    @Headers("apikey: $API_KEY")
    @POST("auth/v1/token?grant_type=password")
    suspend fun auth(@Body creds: AuthCredentials): AuthUser
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