package com.example.contact.model

import android.content.Context
import android.database.Cursor

class SignupPasswordModel(val context: Context) {

    var logInDB = LogInDB(context) //Initializinf Database object

    //    getting details from data base for cross check with data given by user
    private fun getDataFromDB(email : String): Cursor?{
        try{
            return logInDB.getDetailedData(email)
        }catch (e : Exception){
            return null
        }
    }

    //    password and user validation for signup and creating new records for new user
    fun checkSignupPasswordAndWriteDB(name :String, email : String, number : String, password : String, confirmPassword : String ) : String{
        if (password.length <= 6) return "Alert!!,Password should contains more then 6 charecter"
        else if (password.equals(confirmPassword)) {
            val cursor : Cursor? = getDataFromDB(email)
            if (cursor?.count == 0) {
                logInDB.insertuserdata(name, email, password, number)
                return "Congrats!!,You are Registered Successfully!!"
            } else return "Alert!!,User Already Exists You can login by clicking Okay"
        } else return "Alert!!,Password and Confirm password is not same"
    }

}