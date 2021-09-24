package com.example.contact.model

import android.content.Context
import android.database.Cursor
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignupModel(val context: Context) {

    var logInDB = LogInDB(context) //Initializinf Database object
    val emailValidationPattern = "^[a-z][a-z0-9.]{5,30}@[a-z]+[.a-z]+\$"
    val mobileNumberPattern = "(((0)?|\\+([0-9]{0,3})){0,4}([ -]*)(([0-9]{5,15})\$))"

    //    getting details from data base for cross check with data given by user
    private fun getDataFromDB(email : String): Cursor?{
        try{
            return logInDB.getDetailedData(email)
        }catch (e : Exception){
            return null
        }
    }
    //   email Validation
    fun checkEmail(email : String) : Boolean{
        if(Pattern.matches(emailValidationPattern,email)) return true
        return false
    }
    //   MobileNumber Validation
    fun checkMobileNumber(mobileNumber : String) : Boolean{
        if(Pattern.matches(mobileNumberPattern,mobileNumber)) return true
        return false
    }
    fun checkPassword(password: String,confirmPassword: String) : String{
        if (password.length <= 6) return "Alert!!,Password should contains more then 6 charecter"
        else{
            if (!(password.equals(confirmPassword))) {
             return "Alert!!,Password and Confirm password is not same"
            }
        }
        return "Success"
    }

    //    password and user validation for signup and creating new records for new user
    fun checkSignupPasswordAndWriteDB(name :String, emailFromUi : String, number : String, password : String, confirmPassword : String ) : String {
        val email = emailFromUi.lowercase()
        if(checkEmail(email)) {
            if(!(checkMobileNumber(number))) return "Alert!!,Enter valid Mobile Number"
            else if (checkPassword(password, confirmPassword) == "Success") {
                CoroutineScope(IO).launch {
                    val db = Firebase.firestore
                    val userData = hashMapOf(
                        "UserName" to name,
                        "Email" to email,
                        "MobileNumber" to number,
                        "Password" to password
                    )
                    db.collection("user")
                        .add(userData)
                        .addOnSuccessListener {
                            Toast.makeText(context,"Data Added ${it?.id}",Toast.LENGTH_SHORT).show()
                        }
                }
                val cursor: Cursor? = getDataFromDB(email)
                if (cursor?.count == 0) {
                    logInDB.insertuserdata(name, email, password, number)
                    return "Congrats!!,You are Registered Successfully!!"
                } else return "Alert!!,User Already Exists You can login by clicking Okay"
            } else return checkPassword(password, confirmPassword)
        }
        else return "Alert!!,Enter valid Email Address"
    }

}