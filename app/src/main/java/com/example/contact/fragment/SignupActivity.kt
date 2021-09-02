package com.example.contact.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.signup_fragment.view.*

class SignupActivity: Fragment(R.layout.signup_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.signup_fragment, container, false)

        //animation
        val topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.topanimation)
        var bottomAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottomanimation)
        view.register.animation = topAnimation
        view.registerscroll.animation = bottomAnimation

        //Loginfragment
        view.loginup.setOnClickListener {
            var signinFragment = SigninActivity()
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
                    requireContext().let { it1 ->
                        MaterialAlertDialogBuilder(it1)
                            .setTitle(resultData[0])
                            .setMessage(resultData[1])
                            .setNeutralButton("Okay") { dialog, which ->
                                if (resultData[1] == "You are Registered Successfully!!") {
                                    val bundle = Bundle()
                                    bundle.putString("email", view.emailsignup.text.toString())
                                    val signinFragment = SigninActivity()
                                    signinFragment.arguments = bundle
                                    parentFragmentManager.beginTransaction()
                                        .replace(R.id.fragment, signinFragment).commit()
                                } else if (resultData[1] == "User Already Exists You can login by clicking Okay") {
                                    val bundle = Bundle()
                                    bundle.putString("email", view.emailsignup.text.toString())
                                    val signinFragment = SigninActivity()
                                    signinFragment.arguments = bundle
                                    parentFragmentManager.beginTransaction()
                                        .replace(R.id.fragment, signinFragment).commit()
                                }
                            }
                            .setNegativeButton("Cancel") { dialog, which -> }
                            .show()
                    }
                }
            })
        }
        return view
    }
}