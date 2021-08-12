package com.example.contact

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.contact.model.LogInDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.forgetpasswordfragment.view.*
import kotlinx.android.synthetic.main.signin_fragment.view.*
import kotlinx.android.synthetic.main.signup_fragment.view.*
import java.lang.Exception

class ForgetPasswordFragment : Fragment(R.layout.forgetpasswordfragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var logInDB = context?.let { LogInDB(it) }
        var view = inflater.inflate(R.layout.forgetpasswordfragment, container, false)
        view.Emailforget.setText(arguments?.getString("email"))
        view.backtologin.setOnClickListener {
            var signinFragment = SigninActivity()
            parentFragmentManager.beginTransaction().replace(R.id.fragment, signinFragment).commit()
        }
        view.forgetvalidate.setOnClickListener {
            var cursor : Cursor?
            var dbEmail = ""
            var dbMobile = ""
            try{
                cursor = logInDB?.getDetailedData(view.Emailforget.text.toString())
            }catch (e : Exception){
                cursor = null
            }
            try {
                if(cursor != null) {
                    if (cursor.count != 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getString(1) != null) {
                                dbEmail = cursor.getString(1)
                            }
                            if (cursor.getString(3) != null) {
                                dbMobile = cursor.getString(3)
                            }
                        }
                        if (dbEmail.equals(view.Emailforget.text.toString()) && dbMobile.equals(view.forgetMobile.text.toString())) {
                            var createPass = CreatePassworFragment()
                            var bundle = Bundle()
                            bundle.putString("email", view.Emailforget.text.toString())
                            createPass.arguments = bundle
                            parentFragmentManager.beginTransaction().replace(R.id.fragment, createPass).commit()
                        }
                        else if (!(dbEmail.equals(view.Emailforget.text.toString()) && dbMobile.equals(view.forgetMobile.text.toString()))){
                            context?.let { it1 ->
                                MaterialAlertDialogBuilder(it1)
                                    .setTitle("Alert!!")
                                    .setMessage("Email and Mobile Number are not same")
                                    .setNeutralButton("Okay") { dialog, which ->
                                    }
                                    .show()
                            }
                        }
                        else{
                            context?.let { it1 ->
                                MaterialAlertDialogBuilder(it1)
                                    .setTitle("Alert!!")
                                    .setMessage("User Not Exists you can signup by clicking Okay")
                                    .setNeutralButton("Okay") { dialog, which ->
                                        var signupActivity = SignupActivity()
                                        parentFragmentManager.beginTransaction().replace(R.id.fragment, signupActivity).commit()
                                    }
                                    .show()
                            }
                        }
                    }
                    else{
                        context?.let { it1 ->
                            MaterialAlertDialogBuilder(it1)
                                .setTitle("Alert!!")
                                .setMessage("User Not Exists you can signup by clicking Okay")
                                .setNeutralButton("Okay") { dialog, which ->
                                    var signupActivity = SignupActivity()
                                    parentFragmentManager.beginTransaction().replace(R.id.fragment, signupActivity).commit()
                                }
                                .show()
                        }

                    }

                }else{
                    context?.let { it1 ->
                        MaterialAlertDialogBuilder(it1)
                            .setTitle("Alert!!")
                            .setMessage("User Not Exists you can signup by clicking Okay")
                            .setNeutralButton("Okay") { dialog, which ->
                                var signupActivity = SignupActivity()
                                parentFragmentManager.beginTransaction().replace(R.id.fragment, signupActivity).commit()
                            }
                            .show()
                    }

                }

            }catch (e : Exception){
                println(e)
                context?.let { it1 ->
                    MaterialAlertDialogBuilder(it1)
                        .setTitle("Alert!!")
                        .setMessage("User Not Exists")
                        .setNeutralButton("Okay") { dialog, which ->
                        }
                        .show()
                }
            }

        }
        return view
    }
}