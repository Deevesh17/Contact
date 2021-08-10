package com.example.contact.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contact.model.ContactData
import com.example.contact.model.ImportData

class ContactViewModel(val ctx : Context) : ViewModel() {
    var contactDataLive : MutableLiveData<String> = MutableLiveData()
    fun setValutodata(title : String):Boolean{
        contactDataLive.value = title
        return true
    }

}