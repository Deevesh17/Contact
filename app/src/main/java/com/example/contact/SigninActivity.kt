package com.example.contact

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.contact.model.LogInDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.signin_fragment.view.*
import kotlinx.android.synthetic.main.signup_fragment.view.*
import java.lang.Exception

class SigninActivity : Fragment(R.layout.signin_fragment) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var logInDB = context?.let { LogInDB(it) }
        var view = inflater.inflate(R.layout.signin_fragment,container,false)
        view.UserName.setText(arguments?.getString("userName"))
        view.loginsignin.setOnClickListener {
            var cursor : Cursor?
            try{
                cursor = logInDB?.getDetailedData(view.UserName.text.toString())
            }catch (e : Exception){
                cursor = null
            }
            var passoword = ""
            println(cursor)
            try {
                if(cursor != null) {
                    println(cursor.count)
                    if (cursor.count != 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getString(2) != null) {
                                passoword = cursor.getString(2)
                                println(passoword)
                            }
                        }
                        if (passoword.equals(view.Password.text.toString())) {
                            var intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else if (passoword != "" && !passoword.equals(view.Password.text.toString())){
                            context?.let { it1 ->
                                MaterialAlertDialogBuilder(it1)
                                    .setTitle("Alert!!")
                                    .setMessage("Password Not Matched")
                                    .setNeutralButton("Okay") { dialog, which ->
                                    }
                                    .show()
                            }
                        }
                        else{
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
                    else{
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