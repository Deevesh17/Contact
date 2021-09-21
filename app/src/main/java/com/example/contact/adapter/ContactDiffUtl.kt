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
            oldList[oldItemPosition].contactId != newList[newItemPosition].contactId -> {false}
            oldList[oldItemPosition].name != newList[newItemPosition].name -> {false}
            oldList[oldItemPosition].email != newList[newItemPosition].email -> {false}
            oldList[oldItemPosition].contactgroup != newList[newItemPosition].contactgroup -> {false}
            oldList[oldItemPosition].website != newList[newItemPosition].website -> {false}
            oldList[oldItemPosition].number != newList[newItemPosition].number -> {false}
            oldList[oldItemPosition].profile != newList[newItemPosition].profile -> {false}
            oldList[oldItemPosition].notes != newList[newItemPosition].notes -> {false}
            oldList[oldItemPosition].nickname != newList[newItemPosition].nickname -> {false}
            oldList[oldItemPosition].company != newList[newItemPosition].company -> {false}
            oldList[oldItemPosition].address != newList[newItemPosition].address -> {false}
            else -> {true}
        }
    }
}