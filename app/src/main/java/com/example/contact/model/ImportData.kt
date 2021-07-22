package com.example.contact.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore

class ImportData(val ctx: Context) {
    private val contact = ctx.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )
    var contactList : ArrayList<ContactData> = ArrayList<ContactData>()
    fun getContactData() : ArrayList<ContactData>{
        if (contact != null) {
            while (contact.moveToNext()) {
                val personName =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val personNumber =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val personprofile =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                var profile: Bitmap? = null
                if (personprofile != null) {
                    profile = MediaStore.Images.Media.getBitmap(
                        ctx.contentResolver,
                        Uri.parse(personprofile)
                    )
                }
                contactList.add(ContactData(personName, personNumber, profile))
            }
            contact.close()
        }
        return contactList
    }

}