package com.example.contact

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.contact.model.LogInDB
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.signin_fragment.view.*
import java.lang.Exception
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.app.Dialog
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.progreesbar.*
import kotlinx.android.synthetic.main.signin_fragment.*
import kotlinx.android.synthetic.main.signup_fragment.view.*
import java.util.*


class SigninActivity : Fragment(R.layout.signin_fragment) {
    lateinit var progressDialog : Dialog
    override fun onStart() {
        super.onStart()
        val acct = GoogleSignIn.getLastSignedInAccount(activity)
        if (acct != null) {
            Emaillogin.setText(acct.email)
            progressDialog.importdetails.setText("Logging in...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            Timer().schedule(object : TimerTask(){
                override fun run() {
                    progressDialog.dismiss()
                    val intent = Intent(activity,MainActivity::class.java)
                    intent.putExtra("email",Emaillogin.text.toString())
                    startActivity(intent)
                    activity?.finish()
                }
            },3000)

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        progressDialog = context?.let { Dialog(it) }!!
        progressDialog.setContentView(R.layout.progreesbar)
        var logInDB = context?.let { LogInDB(it) }
        var view = inflater.inflate(R.layout.signin_fragment,container,false)
        //request google
        val googleSignin = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN  )
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(activity, googleSignin);

        view.google.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent,101)

        }

        view.Emaillogin.setText(arguments?.getString("userName"))
        view.forgetpass.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("email", view.Emaillogin.text.toString())
            var forgetpass = ForgetPasswordFragment()
            forgetpass.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.fragment, forgetpass).commit()
        }
        view.signupfrag.setOnClickListener {
            var signupFragment = SignupActivity()
            parentFragmentManager.beginTransaction().replace(R.id.fragment, signupFragment).commit()

        }
        view.loginsignin.setOnClickListener {
            var cursor : Cursor?
            try{
                cursor = logInDB?.getDetailedData(view.Emaillogin.text.toString())
            }catch (e : Exception){
                cursor = null
            }
            var passoword = ""
            try {
                if(cursor != null) {
                    if (cursor.count != 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getString(2) != null) {
                                passoword = cursor.getString(2)
                                println(passoword)
                            }
                        }
                        if (passoword.equals(view.Password.text.toString())) {
                            progressDialog.importdetails.setText("Logging in...")
                            progressDialog.setCancelable(false)
                            progressDialog.show()
                            Timer().schedule(object : TimerTask(){
                                override fun run() {
                                    progressDialog.dismiss()
                                    var intent = Intent(context, MainActivity::class.java)
                                    intent.putExtra("email",Emaillogin.text.toString())
                                    startActivity(intent)
                                    activity?.finish()

                                }
                            },3000)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            val acct = GoogleSignIn.getLastSignedInAccount(activity)
            if (acct != null) {
                Emaillogin.setText(acct.email)
            }
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            progressDialog.importdetails.setText("Logging in...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val acct = GoogleSignIn.getLastSignedInAccount(activity)
            if(acct != null) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        progressDialog.dismiss()
                        if (acct != null) {
                            Emaillogin.setText(acct.email)
                        }
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("email", Emaillogin.text.toString())
                        startActivity(intent)
                        activity?.finish()

                    }
                }, 5000)
            }
        } catch (e: ApiException) {
            println(e)
        }
    }
    override fun onPause() {
        super.onPause()
        val acct = GoogleSignIn.getLastSignedInAccount(activity)
        var logInDB = context?.let { LogInDB(it) }
        var cursorg: Cursor?
        try {
            cursorg = logInDB?.getDetailedData(acct.email)
        } catch (e: Exception) {
            cursorg = null
        }
        if (cursorg?.count == 0) {
            if(acct != null) logInDB?.insertuserdata(acct.displayName, acct.email, null, null)
        }
    }
}