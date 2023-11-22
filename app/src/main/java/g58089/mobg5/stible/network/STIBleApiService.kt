package g58089.mobg5.stible.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Base URL of the login server.
 */
private const val BASE_URL = "https://stible.elitios.net"

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