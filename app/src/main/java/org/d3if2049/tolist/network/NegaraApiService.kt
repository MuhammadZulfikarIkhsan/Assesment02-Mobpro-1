package org.d3if2049.tolist.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if2049.tolist.model.Negara
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/" +
        "MuhammadZulfikarIkhsan/jsonapinegara/gh-pages/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NegaraApiService {
    @GET("data-nama-negara.json")
    suspend fun getNegara(): List<Negara>
}

object NegaraApi {
    val service: NegaraApiService by lazy {
        retrofit.create(NegaraApiService::class.java)
    }

    fun getNegaraUrl(nama: String): String {
        return "$BASE_URL$nama.png"
    }
}

enum class ApiStatus  { LOADING, SUCCESS, FAILED }