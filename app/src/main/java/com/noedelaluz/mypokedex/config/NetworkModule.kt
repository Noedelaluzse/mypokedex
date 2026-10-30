package com.noedelaluz.mypokedex.config

import com.noedelaluz.mypokedex.BuildConfig
import com.noedelaluz.mypokedex.config.Constants.API_BASE_URL
import com.noedelaluz.mypokedex.data.network.PokeApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        // El body de las respuestas solo se loguea en debug
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }
        return builder.build()
    }

    fun providePokeApi(): PokeApi = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient())
        .build()
        .create(PokeApi::class.java)
}
