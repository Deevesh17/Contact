package com.example.contact

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.contact.model.ContactData
import com.example.contact.model.LogInDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.signup_fragment.view.*
import java.lang.Exception

class SignupActivity: Fragment(R.layout.signup_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var logInDB = context?.let { LogInDB(it) }
        var view = inflater.inflate(R.layout.signup_fragment,container,false)
        view.loginup.setOnClickListener {
            var signinFragment = SigninActivity()
            parentFragmentManager.beginTransaction().replace(R.id.fragment, signinFragment).commit()
        }
        view.sinupdb.setOnClickListener {
            if(view.signppassword.text.toString().length <=6){
                context?.let { it1 ->
                    MaterialAlertDialogBuilder(it1)
                        .setTitle("Alert!!")
                        .setMessage("Password should contains more then 6 charecter")
                        .setNeutralButton("Okay") { dialog, which ->
                        }
                        .show()
                }
            }
            else if(view.signppassword.text.toString().equals(view.confirmpassword.text.toString())){
                var cursor :Cursor?
                try{
                    cursor = logInDB?.getDetailedData(view.emailsignup.text.toString())
                }catch (e : Exception){
                    cursor = null
                }
                if(cursor?.count == 0) {
                    logInDB?.insertuserdata(view.namesignup.text.toString(),
                        view.emailsignup.text.toString(),
                        view.signppassword.text.toString(),view.signupMobile.text.toString())
                    context?.let { it1 ->
                        MaterialAlertDialogBuilder(it1)
                            .setTitle("Congrats!!")
                            .setMessage("You are Registered Successfully!!")
                            .setNeutralButton("Okay") { dialog, which ->
                                var bundle = Bundle()
                                bundle.putString("userName", view.emailsignup.text.toString())
                                var signinFragment = SigninActivity()
                                signinFragment.arguments = bundle
                                parentFragmentManager.beginTransaction().replace(R.id.fragment, signinFragment).commit()
                            }
                            .show()
                    }
                }else{
                    context?.let { it1 ->
                        MaterialAlertDialogBuilder(it1)
                            .setTitle("Alert!!")
                            .setMessage("User Already Exists You can login by clicking Okay")
                            .setNeutralButton("Okay") { dialog, which ->
                                var bundle = Bundle()
                                bundle.putString("userName", view.emailsignup.text.toString())
                                var signinFragment = SigninActivity()
                                signinFragment.arguments = bundle
                                parentFragmentManager.beginTransaction().replace(R.id.fragment, signinFragment).commit()
                            }
                            .show()
                    }
                }


            }
            else{
                context?.let { it1 ->
                    MaterialAlertDialogBuilder(it1)
                        .setTitle("Alert!!")
                        .setMessage("Password and Confirm password is not same")
                        .setNeutralButton("Okay") { dialog, which ->
                        }
                        .show()
                }
            }
        }

        return view
    }
}