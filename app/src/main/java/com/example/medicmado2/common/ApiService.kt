package com.example.medicmado2.common

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/*
Описание: Интерфейс класса отправки запросов на сервер
Дата создания: 22.03.2023 15:20
Автор: Георгий Хасанов
 */
interface ApiService {
    @Headers(
        "accept: application/json"
    )
    @POST("sendCode")
    suspend fun sendCode(@Header("email") email: String): Response<JsonObject>

    @Headers(
        "accept: application/json"
    )
    @POST("signin")
    suspend fun checkCode(@Header("email") email: String, @Header("code") code: String): Response<JsonObject>

    companion object {
        var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://medic.madskill.ru/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }

            return apiService!!
        }
    }
}