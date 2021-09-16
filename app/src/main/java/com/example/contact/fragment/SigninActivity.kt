package com.example.contact.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.activity.MainActivity
import com.example.contact.viewmodel.ContactViewModel
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.api.services.people.v1.PeopleServiceScopes
import kotlinx.android.synthetic.main.progreesbar.*
import kotlinx.android.synthetic.main.signin_fragment.*
import kotlinx.android.synthetic.main.signin_fragment.view.*
import java.util.*

class SigninActivity : Fragment(R.layout.signin_fragment) {
    lateinit var progressDialog: Dialog
    lateinit var callbackManager: CallbackManager
    lateinit var viewModel : ContactViewModel
    lateinit var email : String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesEditor :SharedPreferences.Editor
    override fun onStart() {
        super.onStart()
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        else if (AccessToken.isCurrentAccessTokenActive()) {
            returnEmailFacebook()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        else{
            if(sharedPreferences.getBoolean("isUserLoggedIn",false)) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        //ProgressBar
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progreesbar)


        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()


        val view = inflater.inflate(R.layout.signin_fragment, container, false)
        view.Emaillogin.setText(sharedPreferences.getString("email",""))


//       Animation
        val topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.topanimation)
        val bottomAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottomanimation)
        view.welcome.animation = topAnimation
        view.userscroll.animation = bottomAnimation


        //facebook login
        callbackManager = CallbackManager.Factory.create()
        view.facebook.setPermissions(listOf("email"))
        view.facebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
//                returnEmailFacebook()
                println(result?.accessToken)
                progressDialog.importdetails.text = "Logging in..."
                progressDialog.setCancelable(false)
                progressDialog.show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        progressDialog.dismiss()
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }, 3000)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }

        })
        val googleResultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleSignInResult(task)
            val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (acct != null) {
                Emaillogin.setText(acct.email)
                sharedPreferencesEditor.putString("email",Emaillogin.text.toString())
            }
        }

        //google login
        val googleSignin = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("653675639171-gdq66smvmp89u2ln6bh2g0bo75ch58fn.apps.googleusercontent.com")
            .requestIdToken("653675639171-gdq66smvmp89u2ln6bh2g0bo75ch58fn.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignin)
        view.google.setOnClickListener {
            if (!AccessToken.isCurrentAccessTokenActive()) {
                val signInIntent = mGoogleSignInClient.signInIntent
                googleResultActivity.launch(signInIntent)
//                startActivityForResult(signInIntent, 101)

            }
        }

        //forgetpasswordScreen
        view.forgetpass.setOnClickListener {
            sharedPreferencesEditor.putString("email",Emaillogin.text.toString())
            val forgetpass = ForgetPasswordFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment, forgetpass).commit()
        }

        //signupScreen
        view.signupfrag.setOnClickListener {
            val signupFragment = SignupActivity()
            parentFragmentManager.beginTransaction().replace(R.id.fragment, signupFragment).commit()
        }

       viewModel = ContactViewModel(requireContext())
        //LoginAuthentication
        view.loginsignin.setOnClickListener {
            viewModel.setPasswordResult(view.Emaillogin.text.toString(),
                view.Password.text.toString())
            viewModel.passwordResult.observe(requireActivity(), androidx.lifecycle.Observer {
                if(it != null) {
                    val resultData = it.split(",")
                    when (it) {
                        "Alert!!,Enter valid Email Address" -> {
                            view.emailsigninphint.error = "Invalid Email"
                        }
                        "Alert!!,Password Not Matched" -> {
                            view.passsignin.error = "Invalid Password"
                        }
                        else -> {
                            requireContext().let { it1 ->
                                MaterialAlertDialogBuilder(it1)
                                    .setTitle(resultData[0])
                                    .setMessage(resultData[1])
                                    .setNeutralButton("Okay") { dialog, which ->
                                        if (resultData[1] == "Success") {
                                            progressDialog.importdetails.text = "Logging in..."
                                            progressDialog.setCancelable(false)
                                            progressDialog.show()
                                            Timer().schedule(object : TimerTask() {
                                                override fun run() {
                                                    progressDialog.dismiss()
                                                    println(Emaillogin.text.toString().lowercase())
                                                    sharedPreferencesEditor.putString(
                                                        "email",
                                                        Emaillogin.text.toString().lowercase()
                                                    )
                                                    sharedPreferencesEditor.putBoolean(
                                                        "isUserLoggedIn",
                                                        true
                                                    )
                                                    val intent = Intent(
                                                        requireContext(),
                                                        MainActivity::class.java
                                                    )
                                                    startActivity(intent)
                                                    requireActivity().finish()
                                                }
                                            }, 3000)
                                        }
                                    }
                                    .setNegativeButton("Cancel") { dialog, which -> }
                                    .show()
                            }
                        }
                    }
                }
            })
        }
        var eyeOpen = true

        view.passsignin.setEndIconOnClickListener {
            if(eyeOpen)
            {
                view.passsignin.setEndIconDrawable(R.drawable.ic_eye_off_svgrepo_com)
                view.Password.inputType = InputType.TYPE_CLASS_TEXT
                eyeOpen = false
            }else {
                view.passsignin.setEndIconDrawable(R.drawable.ic_eye_svgrepo_com)
                view.Password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                view.Password.transformationMethod = PasswordTransformationMethod.getInstance()
                eyeOpen = true
            }
        }
        email = view.Emaillogin.text.toString()

        val ErrorChangeListener = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                view.emailsigninphint.isErrorEnabled = false
                view.passsignin.isErrorEnabled = false
            }

        }
        view.Emaillogin.addTextChangedListener(ErrorChangeListener)
        view.Password.addTextChangedListener(ErrorChangeListener)

        return view
    }


    override fun onPause() {
        super.onPause()
        sharedPreferencesEditor.apply()
        viewModel.setCreateAccount(email)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 102){
            val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (acct != null) {
                Emaillogin.setText(acct.email)
                sharedPreferencesEditor.putString("email", acct.email)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val tk = completedTask.getResult(ApiException::class.java)
//            progressDialog.importdetails.setText("Logging in...")
//            progressDialog.setCancelable(false)
//            progressDialog.show()
            val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if(!(GoogleSignIn.hasPermissions(acct, Scope(PeopleServiceScopes.CONTACTS_READONLY)))){
                GoogleSignIn.requestPermissions(requireActivity(),102,acct,
                    Scope(PeopleServiceScopes.CONTACTS_READONLY)
                )
            }
            else {
                if (acct != null) {
                    Emaillogin.setText(acct.email)
                    sharedPreferencesEditor.putString("email", Emaillogin.text.toString())
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        } catch (e: ApiException) {
            println(e)
        }
    }

    private fun returnEmailFacebook() {
        val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            try {
                Emaillogin.setText(`object`.getString("email"))
            } catch (e: Exception) {
                println(e)
            }
        }
//        val parameters = Bundle()
//        parameters.putString("fields", "id,email")
//        request.parameters = parameters
//        request.executeAsync()
    }
}
