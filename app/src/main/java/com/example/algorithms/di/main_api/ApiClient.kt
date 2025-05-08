package com.example.algorithms.di.main_api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
  //private const val BASE_URL = "http://10.0.2.2:8000/"                            // для локального сервера в эмуляторе
  //private const val BASE_URL = "http://192.168.31.222:8000/"                      //мой ip
  //private const val BASE_URL = "http://rndzy-109-75-135-164.a.free.pinggy.link/"  // пинги
  private const val BASE_URL = "http://5.35.126.14:8000/"                           //wps

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Таймаут на подключение
        .readTimeout(60, TimeUnit.SECONDS)    // Таймаут на чтение данных
        .writeTimeout(60, TimeUnit.SECONDS)   // Таймаут на отправку данных
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}