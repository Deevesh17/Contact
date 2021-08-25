package com.example.contact.model

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Base64
import com.example.contact.viewmodel.ContactViewModel
import java.io.ByteArrayOutputStream

class ContactImportModule(val ctx : Context,val title: ContactViewModel,var selectedList :ArrayList<ContactData>,val signinUser : String) {
    val contactDb = DBHelper(ctx)
    var contactList :ArrayList<ContactData> = ArrayList()
    inner class ContactTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            if(params[0] == "Import" ) {
                val importContact = ImportData(ctx)
                importContact.addContactData()
                contactList = importContact.getContacDatatList()
            }
            else{
                if(selectedList.size > 0){
                    for(contactData in selectedList){
                        val stream = ByteArrayOutputStream()
                        var base64image : String? = null
                        if( contactData.profile != null) {
                            contactData.profile?.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                            val byte = stream.toByteArray()
                            base64image = Base64.encodeToString(byte, Base64.DEFAULT)
                        }
                        contactDb.insertuserdata(
                            contactData.name,
                            contactData.number,
                            base64image,
                            contactData.company,
                            contactData.email,
                            contactData.contactgroup,
                            contactData.address,
                            contactData.nickname,
                            contactData.website,
                            contactData.notes,
                            signinUser
                        ) }
                }
            }
            return params[0]

        }
        override fun onPostExecute(result: String?) {
            if(result == "Import") title.contactList.value = contactList
            else title.SaveResult.value = "Saved"
        }
    }
}