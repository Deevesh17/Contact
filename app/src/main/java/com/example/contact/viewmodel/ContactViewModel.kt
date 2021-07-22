package com.example.contact.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contact.model.ContactData
import com.example.contact.model.ImportData

class ContactViewModel(val ctx : Context) : ViewModel() {
    var contactDataLive : MutableLiveData<ArrayList<ContactData>> = MutableLiveData()
    private fun importData() : ImportData{
        return ImportData(ctx)
    }
    public fun setContactList(){
        contactDataLive.value = importData().getContactData()
    }
    public fun getContactList(): ArrayList<ContactData>{
        return contactDataLive.value!!
    }


}