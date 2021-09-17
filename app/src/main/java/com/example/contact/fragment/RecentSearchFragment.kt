package com.example.contact.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.contact.R
import com.example.contact.adapter.WeatherAdapter
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recentsearchfragment.view.*

class RecentSearchFragment : Fragment(R.layout.recentsearchfragment) {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)

        val user  = sharedPreferences.getString("email","")
        val view = inflater.inflate(R.layout.recentsearchfragment,container,false)

        requireActivity().topAppBarmain.title = "Recent Search"
        requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.exportfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.recent).isVisible = false


        requireActivity().topAppBarmain.setNavigationOnClickListener {
            val weatherFragment = WeatherFragment()
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, weatherFragment).commit()
        }

        val viewModel = ContactViewModel(requireContext())
        user?.let { viewModel.setRecentSearch(it) }
        viewModel.searchList.observe(requireActivity(), Observer {
            view.recentview.adapter = WeatherAdapter(it)
        })
        return view
    }
}