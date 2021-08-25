package com.example.contact.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient {
    var retro : Retrofit? = null

    public fun getClient():Retrofit?{
        when (retro) {
            null -> {
                retro = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }
        return retro
    }
}