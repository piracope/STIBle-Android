package g58089.mobg5.stible.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import g58089.mobg5.stible.model.AuthCredentials
import g58089.mobg5.stible.model.AuthUser
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://dnsrivnxleeqdtbyhftv.supabase.co"

// FIXME: probably not a good idea to store it here
private const val API_KEY =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRuc3Jpdm54bGVlcWR0YnloZnR2Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTY5NzE0MDI2MSwiZXhwIjoyMDEyNzE2MjYxfQ.jgJ49-c9Z8iPQnLVTnPlfRZpKwyBKht-OY8wMTceSiM"

private val json = Json {
    ignoreUnknownKeys = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface STIBleApiService {
    @Headers("apikey: $API_KEY")
    @POST("auth/v1/token?grant_type=password")
    suspend fun auth(@Body creds: AuthCredentials): AuthUser
}

object STIBleApi {
    val retrofitService: STIBleApiService by lazy {
        retrofit.create(STIBleApiService::class.java)
    }
}