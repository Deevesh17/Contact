package com.example.contact.retrofit

import com.google.gson.annotations.SerializedName

class District {

    @SerializedName("weather")
    var weather: ArrayList<Weather> = ArrayList<Weather>()

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("wind")
    var wind: Wind? = null

}
