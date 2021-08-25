package com.example.contact.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.weatherfragment.view.*

class WeatherFragment : Fragment(R.layout.weatherfragment) {
    lateinit var user : String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        user = arguments?.get("email") as String
        val view = inflater.inflate(R.layout.weatherfragment,container,false)
        val viewModel = ContactViewModel(requireContext())

//        navigation button handle in Tolbar
        view.weatherbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

//        Menu details available in toolbar
        view.weatherbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        view.weatherSearch.setOnClickListener {
            user.let { it1 ->
                viewModel.setWeatherrResopnse(view.weatherSearchText.text.toString(),viewModel,
                    it1)
            }
        }
        viewModel.weatherResponce.observe(requireActivity(), Observer {
            if(it != null && it != ""){
                val weatherResponce = it.split(",")
                view.tempC.setText(weatherResponce[1]+" °C")
                view.sky.setText(weatherResponce[0])
                view.air.setText(weatherResponce[2] +" km/h")
            }
        })
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
           R.id.recent ->
           {
               val recent = RecentSearchFragment()
               val bundle = Bundle()
               bundle.putString("email" ,user)
               recent.arguments = bundle
               parentFragmentManager.beginTransaction().replace(R.id.mainfragment,recent).commit()
           }
        }
        return true
    }
}