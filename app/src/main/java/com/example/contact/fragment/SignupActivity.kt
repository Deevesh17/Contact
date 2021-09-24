package com.example.contact.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_signup.view.*

class SignupActivity: Fragment(R.layout.fragment_signup) {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesEditor : SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()

        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        //animation
        val topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.topanimation)
        val bottomAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottomanimation)
        view.register.animation = topAnimation
        view.registerscroll.animation = bottomAnimation

        //Loginfragment
        view.loginup.setOnClickListener {
            val signinFragment = SigninFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment, signinFragment).commit()
        }


        //Sign up with given data
        view.sinupdb.setOnClickListener {
            val viewModel = ContactViewModel(requireContext())
            viewModel.setSignupData(
                view.namesignup.text.toString(),
                view.emailsignup.text.toString(),
                view.signupMobile.text.toString(),
                view.signppassword.text.toString(),
                view.confirmpassword.text.toString()
            )
            viewModel.passwordResult.observe(requireActivity(), androidx.lifecycle.Observer {
                if(it != null) {
                    val resultData = it.split(",")
                    if (it == "Alert!!,Enter valid Email Address") {
                        view.emailsignuphint.error = "Invalid Email"
                    }else if (it == "Alert!!,Enter valid Mobile Number") {
                        view.mobileHint.error = "Invalid Mobile Number"
                    }  else {
                        requireContext().let { it1 ->
                            MaterialAlertDialogBuilder(it1)
                                .setTitle(resultData[0])
                                .setMessage(resultData[1])
                                .setNeutralButton("Okay") { dialog, which ->
                                    if (resultData[1] == "You are Registered Successfully!!") {
                                        sharedPreferencesEditor.putString(
                                            "email",
                                            view.emailsignup.text.toString()
                                        )
                                        val signinFragment = SigninFragment()
                                        parentFragmentManager.beginTransaction()
                                            .replace(R.id.fragment, signinFragment).commit()
                                    } else if (resultData[1] == "User Already Exists You can login by clicking Okay") {
                                        sharedPreferencesEditor.putString(
                                            "email",
                                            view.emailsignup.text.toString()
                                        )
                                        val signinFragment = SigninFragment()
                                        parentFragmentManager.beginTransaction()
                                            .replace(R.id.fragment, signinFragment).commit()
                                    }
                                }
                                .setNegativeButton("Cancel") { dialog, which -> }
                                .show()
                        }
                    }
                }
            })

        }

        val ErrorChangeListener = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                view.emailsignuphint.isErrorEnabled = false
                view.mobileHint.isErrorEnabled = false
            }

        }
        view.emailsignup.addTextChangedListener(ErrorChangeListener)
        view.signupMobile.addTextChangedListener(ErrorChangeListener)

        return view
    }
    override fun onPause() {
        super.onPause()
        sharedPreferencesEditor.apply()
    }
}