package com.example.contact.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.R
import com.example.contact.activity.ContactDataActivity
import com.example.contact.model.ContactData
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_listdesign.view.*
class ContactAdapter(val contact : ArrayList<ContactData>,val title : ContactViewModel,val signinUser : String) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(),
    Filterable {
    var contactIds : ArrayList<String> = ArrayList()
    private var SelectedContact = ArrayList<ContactData>()
    var selectedPosition = ArrayList<Int>()
    var removedPosition = ArrayList<Int>()
    var isSelectAll = false
    var selectedCount = 0
    var searchList = contact
    var ContactList = ArrayList<ContactData>(searchList)
    private var onClickAsSelect = false
    private var contactid = 0
    private lateinit var parent : ViewGroup
    class ContactViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.profilename
        var number = itemView.profilenumber
        var profile = itemView.profileimage
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        this.parent = parent
        val layoutinflater = LayoutInflater.from(parent.context)
        val view  = layoutinflater.inflate(R.layout.activity_listdesign,parent,false)
        return ContactViewHolder(view)
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        if(ContactList.size > 0) {
            holder.name.setText(ContactList[position].name)
            val numberData = ContactList[position].number.split(",")
            holder.number.setText(numberData[0])
            contactid = ContactList[position].contactId
            if (ContactList[position].profile != null) {
                holder.profile.setImageBitmap(ContactList[position].profile)
            } else {
                holder.profile.setImageResource(R.drawable.ic_action_person)
            }
            if (isSelectAll) {
                holder.itemView.savetodb.isVisible = true
                if (!removedPosition.contains(position)) {
                    selectedPosition.add(position)
                }
                onClickAsSelect = true
            }
            holder.itemView.savetodb.isVisible = selectedPosition.contains(position)
            holder.itemView.setOnClickListener {
                if (selectedPosition.size == 0) {
                    onClickAsSelect = false
                }
                if (onClickAsSelect) {
                    if (holder.itemView.savetodb.isVisible) {
                        holder.itemView.savetodb.isVisible = false
                        selectedPosition.remove(position)
                        removedPosition.add(position)
                        selectedCount--
                        SelectedContact.remove(ContactList[position])
                        title.setValutodata("Selected(${selectedCount}/${contact.size})")

                    } else {
                        holder.itemView.savetodb.isVisible = true
                        selectedPosition.add(position)
                        SelectedContact.add(ContactList[position])
                        selectedCount++
                        title.setValutodata("Selected(${selectedCount}/${contact.size})")
                    }
                } else {
                    if (ContactList[position].contactId != -1) {
                        val intent =
                            Intent(holder.itemView.context, ContactDataActivity::class.java)
                        intent.putExtra("contactid", ContactList[position].contactId)
                        intent.putExtra("email", signinUser)
                        startActivity(holder.itemView.context, intent, null)
                    }
                }
                if (selectedCount == contact.size) {
                    isSelectAll = true
                }
            }
            holder.itemView.setOnLongClickListener {
                holder.itemView.savetodb.isVisible = true
                selectedPosition.add(position)
                onClickAsSelect = true
                selectedCount++
                SelectedContact.add(ContactList[position])
                title.setValutodata("Selected(${selectedCount}/${contact.size})")
                if (selectedCount == contact.size) {
                    isSelectAll = true
                }
                true
            }
        }

    }
    override fun getItemCount(): Int {
        return ContactList.size
    }
    fun getSelectedContactList():ArrayList<ContactData>{
       return SelectedContact
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<ContactData>()
                if(constraint?.isEmpty() == true){
                    filteredList.addAll(searchList)
                }else{
                    val filterpattern = constraint.toString().lowercase()
                    for(Data in searchList){
                        for (name in Data.name.split(" ")){
                            if (name.lowercase().startsWith(filterpattern)){
                                filteredList.add(Data)
                            }
                        }
                        for(num in Data.number.split(",")){
                            if(num.lowercase().replace(" ","").contains(filterpattern)){
                                filteredList.add(Data)
                            }
                        }
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = filteredList
                return filterResult
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ContactList.clear()
                ContactList.addAll(results!!.values as ArrayList<ContactData>)
                notifyDataSetChanged()
            }
        }
    }

}
