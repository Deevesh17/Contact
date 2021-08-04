package com.example.contact.model

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Base64
import kotlinx.android.synthetic.main.activity_contact_data.*
import java.io.ByteArrayOutputStream

class ImportData(val ctx: Context) {
    val contactDb = DBHelper(ctx)
    var contactList : ArrayList<ContactData> = ArrayList<ContactData>()
    fun addContactData() {
        val contact = ctx.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (contact != null) {
            while (contact.moveToNext()) {
                val personName =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val personNumber =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val personprofile =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                var profile: Bitmap? = null
                var profileBitmap : String? = null
                if (personprofile != null) {
                    profile = MediaStore.Images.Media.getBitmap(
                        ctx.contentResolver,
                        Uri.parse(personprofile)
                    )
                    var stream = ByteArrayOutputStream()
                    profile?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    var compressedByte = stream.toByteArray()
                    profileBitmap = Base64.encodeToString(compressedByte, Base64.DEFAULT)
                }
                contactDb.insertuserdata(personName,personNumber,profileBitmap,null,null,null,null,null,null,null)
            }
            contact.close()
        }
    }
}