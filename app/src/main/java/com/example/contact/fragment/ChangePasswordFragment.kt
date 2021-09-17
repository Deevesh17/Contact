package com.example.contact.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.changepasswordfragment.view.*

class ChangePasswordFragment : Fragment(R.layout.changepasswordfragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lateinit var sharedPreferences: SharedPreferences

        
        requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.exportfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.recent).isVisible = false
        requireActivity().topAppBarmain.title = "Change Password"
        requireActivity().topAppBarmain.setNavigationIcon(R.drawable.ic_action_goback)

        requireActivity().topAppBarmain.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)

        val user : String? = sharedPreferences.getString("email","")

        val view = inflater.inflate(R.layout.changepasswordfragment,container,false)

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
                            val setting = SettingFragment()
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

        return view
    }
}