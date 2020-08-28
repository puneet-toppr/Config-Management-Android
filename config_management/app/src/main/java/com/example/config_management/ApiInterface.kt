package com.example.config_management

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInterface {
    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.110.1:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }
}