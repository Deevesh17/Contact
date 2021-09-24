package com.example.contact.model

import android.content.Context
import android.database.Cursor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class LoginPasswordModel(val context : Context) {

    var logInDB = LogInDB(context) //Initializinf Database object
    val emailValidationPattern = "^[a-z][a-z0-9.]{5,30}@[a-z]+[.a-z]+\$"
    //    getting details from data base for cross check with data given by user
    private fun getDataFromDB(email : String): Cursor?{
        try{
            return logInDB.getDetailedData(email)
        }catch (e : Exception){
            return null
        }
    }

    fun CreateGoogleAccount(email: String){
        val cursor : Cursor? = getDataFromDB(email)
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        if (cursor?.count == 0) {
            if(acct != null) logInDB.insertuserdata(acct.displayName, acct.email, null, null)
        }
    }
    fun CreateFacebookAccount(email: String){
        Firebase.auth.currentUser.let {
            val cursor: Cursor? = getDataFromDB(email)
            if (cursor?.count == 0) {
                logInDB.insertuserdata(it?.displayName, it?.email, null, null)
            }
        }
    }

//    password validation
    fun checkPassword(userPassword : String,dbPassword : String): String {
        return if (dbPassword == userPassword) "Congrats!!,Success"
            else "Alert!!,Password Not Matched"
    }

//   email Validation
    fun checkEmail(email : String) : Boolean{
        if(Pattern.matches(emailValidationPattern,email)) return true
        return false
    }

    //    email Validation for login page
    fun checkLoginPassword( emailFromUi: String,Originalpassword: String) : String {
        val email = emailFromUi.lowercase()
        if (email != "") {
            if(checkEmail(email)) {
                val cursor: Cursor? = getDataFromDB(email)
                var passoword = ""
                try {
                    if (cursor != null) {
                        if (cursor.count != 0) {
                            while (cursor.moveToNext()) if (cursor.getString(2) != null) passoword =
                                cursor.getString(2)
                            return checkPassword(Originalpassword, passoword)
                        }
                        return "Alert!!,User Not Exists"
                    }
                } catch (e: Exception) {
                    return "Alert!!,User Not Exists"
                }
                return "Alert!!,User Not Exists"
            }
            else return "Alert!!,Enter valid Email Address"
        }
        return "Alert!!,User Not Exists"

    }
}