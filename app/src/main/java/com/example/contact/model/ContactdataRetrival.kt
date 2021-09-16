package com.example.contact.model

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactdataRetrival( val listType : String,var selectedList :ArrayList<ContactData> ,val signinUser : String,ctx : Context,val title: ContactViewModel) {
    val contactDb : DBHelper = DBHelper(ctx)
    private var contactList :ArrayList<ContactData> = ArrayList()
    fun getAndDeleteContactFromDB(){
        CoroutineScope(IO).launch {
            when (listType) {
                "GetDB" -> getFromDB()
                "Delete" -> deleteContact()
            }
        }
    }
    private fun setContactList(contact : ArrayList<ContactData>){
        title.contactList.value = contact
    }
    private suspend fun updateLiveData(contact : ArrayList<ContactData>){
        withContext(Dispatchers.Main){
            setContactList(contact)
        }
    }
    private suspend fun deleteContact(){
        if(selectedList.size > 0){
            for(contactData in selectedList){
                contactDb.deletedata(contactData.contactId)
            }
        }
        getFromDB()
    }
    private suspend fun getFromDB(){
        var cursorTotal = 0
        var cursor: Cursor? = null
        try {
            cursor = contactDb.getdata(signinUser)
            cursorTotal = cursor?.count!!
        }
        catch (e : Exception){
            e.printStackTrace()
        }
        try {
            if (cursorTotal != 0 && cursor != null) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(3) != null) {
                        val compressed = Base64.decode(cursor.getString(3), Base64.DEFAULT)
                        contactList.add(
                            ContactData(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                BitmapFactory.decodeByteArray(compressed,
                                    0,
                                    compressed.size),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getString(7),
                                cursor.getString(8),
                                cursor.getString(9),
                                cursor.getString(10)
                            )
                        )
                    } else {
                        contactList.add(ContactData(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            null,
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                            cursor.getString(10)
                        ))
                    }
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        updateLiveData(contactList)
    }
}