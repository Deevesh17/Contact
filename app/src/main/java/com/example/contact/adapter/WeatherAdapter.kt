package com.example.contact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.R
import kotlinx.android.synthetic.main.recentadapter.view.*

class WeatherAdapter(val searclList : ArrayList<String>) :RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var districtName = itemView.districtName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val view  = layoutinflater.inflate(R.layout.recentadapter,parent,false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
       holder.districtName.setText(searclList[position])
    }

    override fun getItemCount(): Int {
        return searclList.size
    }
}