package com.example.contact.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.contact.R
import com.example.contact.model.RemoteConfigUtills
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment(R.layout.fragment_about) {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.exportfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.recent).isVisible = false
        requireActivity().topAppBarmain.title = "About"
        requireActivity().topAppBarmain.setBackgroundColor(Color.parseColor(RemoteConfigUtills.getAudioToolBarBackground()))
        requireActivity().topAppBarmain.setNavigationIcon(R.drawable.ic_action_goback)
        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)
        val user :String? = sharedPreferences.getString("email","")
        val view = inflater.inflate(R.layout.fragment_about,container,false)
        requireActivity().topAppBarmain.setNavigationOnClickListener {
            val settingFragment = SettingFragment()
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, settingFragment).commit()
        }
        val viewmodel = ContactViewModel(requireContext())
        user?.let { viewmodel.setAboutUser(it) }
        viewmodel.SaveResult.observe(requireActivity(), Observer {
            if(it!= null && it != "")
            {
                val result = it.split(",")
                view.emailAbout.setText(result[1])
                view.userNameAbout.setText(result[0])
                if (result[2] != "null" && result[2] != "") view.mobileAbout.setText(result[2])
            }
        })
        return view
    }
}