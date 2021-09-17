package com.example.contact.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.weatherfragment.view.*

class WeatherFragment : Fragment(R.layout.weatherfragment) {
    var user : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        lateinit var sharedPreferences: SharedPreferences

        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)

        user = sharedPreferences.getString("email","")
        val view = inflater.inflate(R.layout.weatherfragment,container,false)


        requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.exportfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.recent).isVisible = true
        requireActivity().topAppBarmain.title = "Weather"
        requireActivity().topAppBarmain.setNavigationIcon(R.drawable.ic_action_goback)

        val viewModel = ContactViewModel(requireContext())

//        navigation button handle in Tolbar
        requireActivity().topAppBarmain.setNavigationOnClickListener {
            val listfrag = ContactListViewFragment()
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, listfrag).commit()
        }

//        Menu details available in toolbar
        requireActivity().topAppBarmain.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        view.weatherSearch.setOnClickListener {

            user?.let { it1 ->
                viewModel.setWeatherrResopnse(view.weatherSearchText.text.toString(),viewModel,
                    it1
                )
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
               parentFragmentManager.beginTransaction().replace(R.id.mainfragment,recent).commit()
           }
        }
        return true
    }
}