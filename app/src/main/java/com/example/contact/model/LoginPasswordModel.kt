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
        println(email)
        val cursor : Cursor? = getDataFromDB(email)
        println(cursor)
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        if (cursor?.count == 0) {
            println(cursor.count)
            if(acct != null) {
                logInDB.insertuserdata(acct.displayName, acct.email, null, null)
            }
            if(AccessToken.isCurrentAccessTokenActive()) logInDB.insertuserdata(null,email, null, null)
        }
    }

    //    Password Validation for login page
    fun checkLoginPassword(email: String,Originalpassword: String) : String{

        val cursor : Cursor? = getDataFromDB(email)
        var passoword = ""
        try {
            if(cursor != null) {
                if (cursor.count != 0) {
                    while (cursor.moveToNext()) if (cursor.getString(2) != null) passoword = cursor.getString(2)
                    if (passoword.equals(Originalpassword)) {
                        return "Congratss!!,Success"
                    }
                    else if (passoword != "" && !passoword.equals(Originalpassword)) return "Alert!!,Password Not Matched"
                    return "Alert!!,User Not Exists"
                }
                return "Alert!!,User Not Exists"
            }
        }catch (e : Exception){
            return "Alert!!,User Not Exists"
        }
        return "Alert!!,User Not Exists"
    }
}