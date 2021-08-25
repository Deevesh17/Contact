package com.example.contact.model

import android.content.Context
import android.database.Cursor

class ForgetPasswordModel(ctx : Context) {

    var logInDB = LogInDB(ctx) //Initializinf Database object

    //    getting details from data base for cross check with data given by user
    private fun getDataFromDB(email: String): Cursor? {
        try {
            return logInDB.getDetailedData(email)
        } catch (e: Exception) {
            return null
        }
    }

    //  mobile number and email validation and cross checking the mobile number
    fun checkForgetPasswordUser(email: String, mobileNumber: String): String {
        val cursor: Cursor? = getDataFromDB(email)
        var dbEmail = ""
        var dbMobile = ""
        try {
            if (cursor != null) {
                if (cursor.count != 0) {
                    while (cursor.moveToNext()) {
                        if (cursor.getString(1) != null) dbEmail = cursor.getString(1)
                        if (cursor.getString(3) != null) dbMobile = cursor.getString(3)
                    }
                    if (dbEmail.equals(email) && dbMobile.equals(mobileNumber)) return "Success"
                    else if (!(dbEmail.equals(email) && dbMobile.equals(mobileNumber))) return "Alert!!,Email and Mobile Number are not same"
                }
            } else return "Alert!!,User Not Exists you can signup by clicking Okay"
        } catch (e: Exception) {
            return "Alert!!,User Not Exists you can signup by clicking Okay"
        }
        return "Alert!!,User Not Exists you can signup by clicking Okay"
    }
}