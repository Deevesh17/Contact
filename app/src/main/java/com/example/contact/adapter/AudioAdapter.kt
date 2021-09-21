package com.example.contact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.R
import com.example.contact.musicroomdb.Audio
import kotlinx.android.synthetic.main.recentadapter.view.*

class AudioAdapter : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    private var oldList : List<Audio> = emptyList()

    class AudioViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var audiotitle: TextView = itemView.viewTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val view  = layoutinflater.inflate(R.layout.recentadapter,parent,false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.audiotitle.setText(oldList[position].audioTitle)
    }

    override fun getItemCount(): Int {
        return oldList.size
    }
    fun setData(newContactList: List<Audio>){
        val audioDiffUtil = AudioDiffUtil(oldList,newContactList)
        val diffUtilResult = DiffUtil.calculateDiff(audioDiffUtil)
        oldList = newContactList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}