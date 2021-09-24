package com.example.contact.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.contact.model.ContactData

class ContactDiffUtl(val oldList :ArrayList<ContactData>, val newList : ArrayList<ContactData>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].contactId == newList[newItemPosition].contactId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition] != newList[newItemPosition] -> {false}
            else -> {true}
        }
    }
}