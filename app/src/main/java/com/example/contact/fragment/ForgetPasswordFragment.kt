package com.example.contact.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.model.LogInDB
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.forgetpasswordfragment.view.*

class ForgetPasswordFragment : Fragment(R.layout.forgetpasswordfragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var logInDB = context?.let { LogInDB(it) }

        var view = inflater.inflate(R.layout.forgetpasswordfragment, container, false)

//        Annimation
        val topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.topanimation)
        val bottomAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottomanimation)
        view.forgetTitle.animation = topAnimation
        view.forgetScroll.animation = bottomAnimation

        view.Emailforget.setText(arguments?.getString("email"))

//        back to login
        view.backtologin.setOnClickListener {
            var signinFragment = SigninActivity()
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
                        val bundle = Bundle()
                        bundle.putString("email", view.Emailforget.text.toString())
                        createPass.arguments = bundle
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