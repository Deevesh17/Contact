package com.example.contact.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.createnewpassword.view.*

class CreatePassworFragment : Fragment(R.layout.createnewpassword) {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.createnewpassword, container, false)



        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)

//       Animation
        val topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.topanimation)
        val bottomAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottomanimation)
        view.createFragment.animation = topAnimation
        view.CreateScroll.animation = bottomAnimation

//        getting email from forget password page
        val email: String? = sharedPreferences.getString("email","")

//        Back to Login page
        view.backtologinpass.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment, SigninActivity()).commit()
        }

        view.creatpass.setOnClickListener {
            val viewModel = ContactViewModel(requireContext())
            email?.let { it1 -> viewModel.setNewPassword(it1,view.newpassword.text.toString(),view.newconfirm.text.toString()) }
            viewModel.passwordResult.observe(requireActivity(), Observer {
                if(it != null){
                    val resultData = it.split(",")
                    if (resultData[0] == "true"){
                        Toast.makeText(context, "Password Successfully changed", Toast.LENGTH_SHORT)
                            .show()
                        parentFragmentManager.beginTransaction().replace(R.id.fragment, SigninActivity())
                            .commit()
                    }else{
                        requireContext().let { it1 ->
                            MaterialAlertDialogBuilder(it1)
                                .setTitle(resultData[0])
                                .setMessage(resultData[1])
                                .setNeutralButton("Okay") { dialog, which ->
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