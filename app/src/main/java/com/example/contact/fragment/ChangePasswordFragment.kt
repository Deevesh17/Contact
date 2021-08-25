package com.example.contact.fragment

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
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.changepasswordfragment.view.*

class ChangePasswordFragment : Fragment(R.layout.changepasswordfragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val user : String? = arguments?.get("email") as String?

        val view = inflater.inflate(R.layout.changepasswordfragment,container,false)

//        Animation
        val topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.topanimation)
        val bottomAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bottomanimation)
        view.fragname.animation = topAnimation
        view.changescroll.animation = bottomAnimation

//        Checking the user from google or facebook or normal user
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        view.changepass.setOnClickListener {
            if (!(acct != null || AccessToken.isCurrentAccessTokenActive())) {
                val viewModel = ContactViewModel(requireContext())
                user.let { it1 ->
                    it1?.let { it2 ->
                        viewModel.setChangePassword(view.oldpassword.text.toString(),
                            it2,
                            view.changenewpassword.text.toString(),
                            view.changenewconfirm.text.toString())
                    }
                }
                viewModel.passwordResult.observe(requireActivity(), Observer {
                    if (it != null) {
                        val resultData = it.split(",")
                        if (resultData[0] == "true") {
                            Toast.makeText(context,
                                "Password Successfully changed",
                                Toast.LENGTH_SHORT)
                                .show()
                            var setting = SettingFragment()
                            val bundle = Bundle()
                            bundle.putString("email",user)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.mainfragment, setting)
                                .commit()
                        } else {
                            requireContext().let { it1 ->
                                MaterialAlertDialogBuilder(it1)
                                    .setTitle(resultData[0])
                                    .setMessage(resultData[1])
                                    .setNeutralButton("Okay") { dialog, which ->
                                    }
                                    .setNegativeButton("Cancel") { dialog, which -> }
                                    .show()
                            }
                        }
                    }
                })
            }
        }

//        Move to settings fragment
        view.backtosetting.setOnClickListener {
            val setting = SettingFragment()
            val bundle = Bundle()
            bundle.putString("email",user)
            setting.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment,setting).commit()
        }
        return view
    }
}