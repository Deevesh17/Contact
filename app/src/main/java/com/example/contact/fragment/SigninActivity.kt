package com.example.contact.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.progreesbar.*
import kotlinx.android.synthetic.main.signin_fragment.*
import kotlinx.android.synthetic.main.signin_fragment.view.*
import java.util.*

class SigninActivity : Fragment(R.layout.signin_fragment) {
    lateinit var progressDialog: Dialog
    lateinit var callbackManager: CallbackManager
    lateinit var viewModel : ContactViewModel
    lateinit var email : String
    override fun onStart() {
        super.onStart()
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("email", Emaillogin.text.toString())
            startActivity(intent)
            requireActivity().finish()
        }
        if (AccessToken.isCurrentAccessTokenActive()) {
            returnEmailFacebook()
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("email", Emaillogin.text.toString())
            startActivity(intent)
            requireActivity().finish()
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

        val view = inflater.inflate(R.layout.signin_fragment, container, false)
        view.Emaillogin.setText(arguments?.getString("email"))

//       Animation
        val topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.topanimation)
        val bottomAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottomanimation)
        view.welcome.animation = topAnimation
        view.userscroll.animation = bottomAnimation


        //facebook login
        callbackManager = CallbackManager.Factory.create();
        view.facebook.setPermissions(listOf("email"))
        view.facebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
//                returnEmailFacebook()
                progressDialog.importdetails.setText("Logging in...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        progressDialog.dismiss()
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("email", Emaillogin.text.toString())
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

        //google login
        val googleSignin = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignin);
        view.google.setOnClickListener {
            if (!AccessToken.isCurrentAccessTokenActive()) {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, 101)
            }
        }

        //forgetpasswordScreen
        view.forgetpass.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("email", view.Emaillogin.text.toString())
            val forgetpass = ForgetPasswordFragment()
            forgetpass.arguments = bundle
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
                    requireContext().let { it1 ->
                        MaterialAlertDialogBuilder(it1)
                            .setTitle(resultData[0])
                            .setMessage(resultData[1])
                            .setNeutralButton("Okay") { dialog, which ->
                                if (resultData[1] == "Success") {
                                    progressDialog.importdetails.setText("Logging in...")
                                    progressDialog.setCancelable(false)
                                    progressDialog.show()
                                    Timer().schedule(object : TimerTask() {
                                        override fun run() {
                                            progressDialog.dismiss()
                                            val intent = Intent(requireContext(), MainActivity::class.java)
                                            println(Emaillogin.text.toString())
                                            intent.putExtra("email", Emaillogin.text.toString())
                                            startActivity(intent)
                                            activity?.finish()
                                        }
                                    }, 3000)
                                }
                            }
                            .setNegativeButton("Cancel"){dialog,which ->}
                            .show()
                    }
                }
            })
        }
        email = view.Emaillogin.text.toString()
        return view
    }

    override fun onPause() {
        super.onPause()
        viewModel.setCreateAccount(email)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (acct != null) {
                Emaillogin.setText(acct.email)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            completedTask.getResult(ApiException::class.java)
            progressDialog.importdetails.setText("Logging in...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (acct != null) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        progressDialog.dismiss()
                        Emaillogin.setText(acct.email)
                        val intent = Intent(requireContext(), MainActivity::class.java)
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
