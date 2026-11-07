package com.yaycate.animetec.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {

    // Debe terminar en "/"
    private const val BASE_URL = "https://kitsu.io/api/edge/"

    private val json = Json {
        ignoreUnknownKeys = true   // la API trae muchos campos que no usamos
        coerceInputValues = true
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor { chain ->
            // Kitsu exige este Accept; sin él responde 406
            val request = chain.request().newBuilder()
                .header("Accept", "application/vnd.api+json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        )
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}