package com.example.contact

import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.model.ContactData
import kotlinx.android.synthetic.main.activity_listdesign.view.*
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList

class ContactAdapter(val contact : ArrayList<ContactData>) : RecyclerView.Adapter<ContactAdapter.contactViewHolder>(){
    class contactViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.profilename
        var number = itemView.profilenumber
        var profile = itemView.profileimage
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contactViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val view  = layoutinflater.inflate(R.layout.activity_listdesign,parent,false)
        return contactViewHolder(view)
    }
    override fun onBindViewHolder(holder: contactViewHolder, position: Int) {
        holder.name.setText(contact[position].name)
        holder.number.setText(contact[position].number)
        if(contact[position].profile != null)
        {
            holder.profile.setImageBitmap(contact[position].profile)
        }
        else{
            holder.profile.setImageResource(R.drawable.ic_action_person)
        }
        holder.itemView.setOnClickListener(){
            var intent : Intent = Intent(holder.itemView.context,ContactDataActivity::class.java)
            intent.putExtra("contactid",contact[position].contactId)
            startActivity(holder.itemView.context,intent,null)
        }
    }
    override fun getItemCount(): Int {
        return contact.size
    }

}
