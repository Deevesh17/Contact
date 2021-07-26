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
    fun getdatafromDb() : ArrayList<ContactData> {
        var cursor: Cursor? = contactDb.getdata()
        if (cursor?.count != 0) {
            while (cursor?.moveToNext()!!) {
                if (cursor.getString(2) != null) {
                    var comressed = Base64.decode(cursor.getString(2), Base64.DEFAULT)
                    contactList.add(
                        ContactData(
                            cursor.getString(0),
                            cursor.getString(1),
                            BitmapFactory.decodeByteArray(comressed, 0, comressed.size),cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9)
                        )
                    )
                } else {
                    contactList.add(ContactData(
                        cursor.getString(0),
                        cursor.getString(1),
                        null,
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                    ))
                }
            }
        }
        return contactList

    }
}