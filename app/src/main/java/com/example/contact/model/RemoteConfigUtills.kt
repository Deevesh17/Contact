package com.example.contact.model

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfigUtills {

    private val defaultValueForRemoteConfig : Map<String,String> = hashMapOf("contactTitle" to "Contact","toolBarColour" to "#A4CAF3")

    lateinit var remoteConfig: FirebaseRemoteConfig

    fun initializeRemoteConfig(){
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 2
        }
        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(defaultValueForRemoteConfig)
            fetchAndActivate().addOnCompleteListener {
                println(it.result)
            }
        }
    }
    fun getContactTitle() : String = remoteConfig.getString("contactTitle")
    fun getAudioToolBarBackground() : String = remoteConfig.getString("toolBarColour")


}