package com.example.contact.model

import android.content.Context
import android.database.Cursor

class CheckPassword(val context: Context){

    var logInDB = LogInDB(context) //Initializinf Database object

//    getting details from data base for cross check with data given by user
    private fun getDataFromDB(email : String): Cursor?{
        try{
            return logInDB.getDetailedData(email)
        }catch (e : Exception){
            return null
        }
    }


//    creating new password and validating the new password and updating to existing record
    fun createNewPassword(email : String,password : String, confirmPassword : String) : String{
        if(password.length <=6) return "Alert!!,Password should contains more then 6 charecter"
        else if(password.equals(confirmPassword)){
        val result = logInDB.updateuserdata(email,password)
        if(result) return "true"
        }
        return  "Alert!!,Password and Confirm password is not same"
    }

//    change new password
    fun changeNewPassword(oldPassword : String,email : String,password : String, confirmPassword : String) : String  {
        println("$oldPassword,$email,$password,$confirmPassword")
        val cursor: Cursor? = getDataFromDB(email)
        var dbPassword = ""
        println(cursor)
        if (cursor != null) {
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(2) != null) dbPassword = cursor.getString(2)
                }
            }
        }
        if (dbPassword == oldPassword){
            return createNewPassword(email, password, confirmPassword)
        }
        return "Alert!!,User Not Exists you can signup by clicking Okay"
    }
}