package com.example.contact.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.contact.R
import com.example.contact.adapter.WeatherAdapter
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.recentsearchfragment.view.*

class RecentSearchFragment : Fragment(R.layout.recentsearchfragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user  = arguments?.get("email").toString()

        val view = inflater.inflate(R.layout.recentsearchfragment,container,false)

        view.recentBar.setNavigationOnClickListener {
            var weatherFragment = WeatherFragment()
            val bundle = Bundle()
            bundle.putString("email", user.toString())
            weatherFragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, weatherFragment).commit()
        }

        val viewModel = ContactViewModel(requireContext())
        viewModel.setRecentSearch(user)
        viewModel.searchList.observe(requireActivity(), Observer {
            view.recentview.adapter = WeatherAdapter(it)
        })
        return view
    }
}