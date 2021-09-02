package com.example.contact.model

import android.content.Context
import android.database.Cursor
import com.example.contact.retrofit.District
import com.example.contact.retrofit.WeatherClient
import com.example.contact.retrofit.WeatherService
import com.example.contact.viewmodel.ContactViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherResponceModel(val ctx :Context) {
    val logInDB = LogInDB(ctx)

    private fun getDataFromDB(email : String): Cursor?{
        try{
            return logInDB.getDetailedsearch(email)
        }catch (e : Exception){
            return null
        }
    }
    fun getWeather(districName : String,viewModel: ContactViewModel,user :String) {
        val cursor = getDataFromDB(user)
        if(cursor != null){
            println(cursor.count)
            logInDB.deletesearch(user,districName)
            logInDB.insertWeatherDetails(user,districName)
        }
        else logInDB.insertWeatherDetails(user,districName)
        val weatherService = WeatherClient().getClient()?.create(WeatherService::class.java)
        val dis : Call<District> = weatherService?.getWeather(districName)!!
        dis.enqueue(object  : Callback<District> {
            override fun onResponse(call: Call<District>, response: Response<District>) {
                if (response.code() == 200){
                    val weatherResponse: District = response.body()!!
                    viewModel.weatherResponce.value = weatherResponse.weather[0].main +"," +String.format("%.2f", weatherResponse.main?.temp?.minus(273.15)) +","+ weatherResponse.wind?.speed
                }
            }
            override fun onFailure(call: Call<District>, t: Throwable) {
                println("Error ${t.message}")
            }
        })
    }
}