package com.example.contact.model

import android.content.Context
import android.database.Cursor

class AboutUserModel(val context: Context) {

    var logInDB = LogInDB(context) //Initializinf Database object

    var userData = ""
    //    getting details from data base for cross check with data given by user
    private fun getDataFromDB(email : String): Cursor?{
        try{
            return logInDB.getDetailedData(email)
        }catch (e : Exception){
            return null
        }
    }
    fun getUserDetails(email : String) : String{
        val cursor: Cursor? = getDataFromDB(email)
        if(cursor != null) {
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    userData = cursor.getString(0) +","+cursor.getString(1) +","+cursor.getString(3)
                }
            }
        }
        return userData
    }
}