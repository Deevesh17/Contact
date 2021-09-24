package com.example.contact.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.contact.musicroomdb.Audio

class AudioDiffUtil(val oldList :List<Audio>, val newList : List<Audio>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].audioId == newList[newItemPosition].audioId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition] != newList[newItemPosition] -> {false}
            else -> {true}
        }
    }
}