package com.example.contact.retrofit

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("main")
    var main: String? = null
}