package com.example.contact

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.contact.model.LogInDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.createnewpassword.view.*
import kotlinx.android.synthetic.main.signup_fragment.view.*
import java.lang.Exception

class CreatePassworFragment : Fragment(R.layout.createnewpassword) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var logInDB = context?.let { LogInDB(it) }
        var view = inflater.inflate(R.layout.createnewpassword, container, false)
        var email = arguments?.getString("email")
        view.backtologinpass.setOnClickListener {
            var signinFragment = SigninActivity()
            parentFragmentManager.beginTransaction().replace(R.id.fragment, signinFragment).commit()
        }
        view.creatpass.setOnClickListener {
            if(view.newpassword.text.toString().length <=6){
                context?.let { it1 ->
                    MaterialAlertDialogBuilder(it1)
                        .setTitle("Alert!!")
                        .setMessage("Password should contains more then 6 charecter")
                        .setNeutralButton("Okay") { dialog, which ->
                        }
                        .show()
                }
            }
            else if(view.newpassword.text.toString().equals(view.newconfirm.text.toString())){
                var result = logInDB?.updateuserdata(email,view.newpassword.text.toString())
                if(result == true) {
                    Toast.makeText(context, "Password Sucessfully changed", Toast.LENGTH_SHORT)
                        .show()
                    var signinFragment = SigninActivity()
                    parentFragmentManager.beginTransaction().replace(R.id.fragment, signinFragment)
                        .commit()
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