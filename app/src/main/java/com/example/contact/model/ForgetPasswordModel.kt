package com.example.contact.model

import android.content.Context
import android.database.Cursor
import java.util.regex.Pattern

class ForgetPasswordModel(ctx : Context) {

    var logInDB = LogInDB(ctx) //Initializinf Database object
    val emailValidationPattern = "^[a-z][a-z0-9.]{5,30}@[a-z]+[.a-z]+\$+"
    val mobileNumberPattern =
        "(((0)?|\\+([0-9]{0,3})){0,4}([ -]*)(([0-9]{5,15})\$))"
    //    getting details from data base for cross check with data given by user
    private fun getDataFromDB(email: String): Cursor? {
        try {
            return logInDB.getDetailedData(email)
        } catch (e: Exception) {
            return null
        }
    }
    fun checkEmail(email : String) : Boolean{
        if(Pattern.matches(emailValidationPattern,email)) return true
        return false
    }
    fun checkMobileNumber(mobileNumber : String) : Boolean{
        if(Pattern.matches(mobileNumberPattern,mobileNumber)) return true
        return false

    }

//    validate email and mobile
    fun checkMobileNumber(email : String,dbEmail : String,dbMobile : String,mobileNumber: String):String{
        if (dbEmail.equals(email) && dbMobile.equals(mobileNumber)) return "Success"
        else return "Alert!!,Email and Mobile Number are not same"
    }

    //  mobile number and email validation and cross checking the mobile number
    fun checkForgetPasswordUser(email: String, mobileNumber: String): String {
        if(checkEmail(email)) {
            if(!(checkMobileNumber(mobileNumber))) return "Alert!!,Enter valid Mobile Number"
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
                        return checkMobileNumber(email, dbEmail, dbMobile, mobileNumber)

                    }
                } else return "Alert!!,User Not Exists you can signup by clicking Okay"
            } catch (e: Exception) {
                return "Alert!!,User Not Exists you can signup by clicking Okay"
            }
        }else return "Alert!!,Enter valid Email Address"
        return "Alert!!,User Not Exists you can signup by clicking Okay"
    }
}