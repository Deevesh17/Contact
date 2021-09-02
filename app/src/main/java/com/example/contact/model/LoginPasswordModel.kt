package com.example.contact.model

import android.content.Context
import android.database.Cursor
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn

class LoginPasswordModel(val context : Context) {

    var logInDB = LogInDB(context) //Initializinf Database object

    //    getting details from data base for cross check with data given by user
    private fun getDataFromDB(email : String): Cursor?{
        try{
            return logInDB.getDetailedData(email)
        }catch (e : Exception){
            return null
        }
    }

    fun CreateAccount(email: String){
        val cursor : Cursor? = getDataFromDB(email)
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        if (cursor?.count == 0) {
            if(acct != null) {
                logInDB.insertuserdata(acct.displayName, acct.email, null, null)
            }
            if(AccessToken.isCurrentAccessTokenActive()) logInDB.insertuserdata(null,email, null, null)
        }
    }

//    password validation
    fun checkPassword(userPassword : String,dbPassword : String): String {
        if (dbPassword.equals(userPassword)) return "Congratss!!,Success"
        else return "Alert!!,Password Not Matched"
    }

    //    email Validation for login page
    fun checkLoginPassword(email: String,Originalpassword: String) : String {
        println(email)
        if (email != "") {
            val cursor: Cursor? = getDataFromDB(email)
            var passoword = ""
            try {
                if (cursor != null) {
                    if (cursor.count != 0) {
                        println(cursor.count)
                        while (cursor.moveToNext()) if (cursor.getString(2) != null) passoword =
                            cursor.getString(2)
                        return checkPassword(Originalpassword, passoword)
                    }
                    return "Alert!!,User Not Exists"
                }
            } catch (e: Exception) {
                return "Alert!!,User Not Exists"
            }
            return  "Alert!!,User Not Exists"
        }
        return "Alert!!,User Not Exists"

    }
}