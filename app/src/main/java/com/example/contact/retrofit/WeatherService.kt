package com.example.contact.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather?appid=39691b7e482a1991642200fc930fe706")
    fun getWeather(
        @Query("q") district: String
    ): Call<District>

}