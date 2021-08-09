package com.example.contact

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.model.ContactData
import kotlinx.android.synthetic.main.activity_listdesign.view.*
import kotlin.collections.ArrayList

class ContactAdapter(val contact : ArrayList<ContactData>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(){
    private var SelectedContact = ArrayList<ContactData>()
    private var selectedPosition = ArrayList<Int>()
    class ContactViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.profilename
        var number = itemView.profilenumber
        var profile = itemView.profileimage
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val view  = layoutinflater.inflate(R.layout.activity_listdesign,parent,false)
        return ContactViewHolder(view)
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.name.setText(contact[position].name)
        val numberData = contact[position].number.split(",")
        holder.number.setText(numberData[0])
        if(contact[position].profile != null)
        {
            holder.profile.setImageBitmap(contact[position].profile)
        }
        else{
            holder.profile.setImageResource(R.drawable.ic_action_person)
        }
        if(selectedPosition.contains(position))
            holder.itemView.setBackgroundColor(Color.parseColor("#90ee90"))
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
        holder.itemView.setOnClickListener{
            if(contact[position].contactId != -1) {
                val intent = Intent(holder.itemView.context, ContactDataActivity::class.java)
                intent.putExtra("contactid", contact[position].contactId)
                startActivity(holder.itemView.context, intent, null)
            }
            if(contact[position].contactId == -1){
                selectedPosition.remove(position)
                SelectedContact.remove(contact[position])
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }
        holder.itemView.setOnLongClickListener {
            holder.itemView.setBackgroundColor(Color.parseColor("#90ee90"))
            selectedPosition.add(position)
            SelectedContact.add(contact[position])
        }
    }
    override fun getItemCount(): Int {
        return contact.size
    }
    fun getSelectedContactList():ArrayList<ContactData>{
       return SelectedContact
    }

}
