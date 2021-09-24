package com.example.contact.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_forgetpassword.view.*

class ForgetPasswordFragment : Fragment(R.layout.fragment_forgetpassword) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)

        val view = inflater.inflate(R.layout.fragment_forgetpassword, container, false)

//        Annimation
        val topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.topanimation)
        val bottomAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottomanimation)
        view.forgetTitle.animation = topAnimation
        view.forgetScroll.animation = bottomAnimation

        view.Emailforget.setText(sharedPreferences.getString("email",""))

//        back to login
        view.backtologin.setOnClickListener {
            val signinFragment = SigninFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment, signinFragment).commit()
        }

//        validate email and mobile number
        view.forgetvalidate.setOnClickListener {
            val viewModel = ContactViewModel(requireContext())
            viewModel.setForgetData(view.Emailforget.text.toString(),view.forgetMobile.text.toString())
            viewModel.passwordResult.observe(requireActivity(), androidx.lifecycle.Observer {
                if(it != null) {
                    val resultData = it.split(",")
                    if( resultData[0] == "Success"){
                        val createPass = CreatePassworFragment()
                        parentFragmentManager.beginTransaction().replace(R.id.fragment, createPass)
                            .commit()
                    }
                    else{
                        requireContext().let { it1 ->
                            MaterialAlertDialogBuilder(it1)
                                .setTitle(resultData[0])
                                .setMessage(resultData[1])
                                .setNeutralButton("Okay") { dialog, which ->
                                    if(resultData[1] == "User Not Exists you can signup by clicking Okay") {
                                        val signupActivity = SignupActivity()
                                        parentFragmentManager.beginTransaction()
                                            .replace(R.id.fragment, signupActivity).commit()
                                    }
                                }
                                .setNegativeButton("Cancel"){dialog,which ->}
                                .show()
                        }
                    }
                }
            })
        }
        return view
    }
}