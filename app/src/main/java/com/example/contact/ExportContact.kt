package com.example.contact

import android.content.ContentProviderOperation
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Base64
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contact.model.DBHelper
import java.io.ByteArrayOutputStream

class ExportContact(var context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val contactDb = DBHelper(context)
    override fun doWork(): Result {
        export()
        return Result.success()
    }
    private fun export() {
        val cursorTotal: Int
        val cursor: Cursor? = contactDb.getdata()
        cursorTotal = cursor?.count!!
        var count = 0
        if (cursorTotal != 0) {
            while (cursor.moveToNext()) {
                val contactList: ArrayList<ContentProviderOperation> = ArrayList()
                contactList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build())
                contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        cursor.getString(1)).build())
                val numberlist = cursor.getString(2).split(",")
                for (number in numberlist) {
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)

                        .build())
                }
                if (cursor.getString(3) != null) {
                    val comressed = Base64.decode(cursor.getString(3), Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(comressed, 0, comressed.size)
                    val resolver = applicationContext.contentResolver
//                    var filePath = Environment.getExternalStorageDirectory()
//                    var folder = File(filePath.absolutePath+"/Contactimages/")
//                    folder.mkdir()
//                    var file = File(folder,System.currentTimeMillis().toString()+".jpg")
//                    var outPutStream = FileOutputStream(file)
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,outPutStream)
//                    println(file)
//                    println(outPutStream)
//                    outPutStream.flush()
//                    outPutStream.close()

//                    var contentValue = ContentValues()
//                    contentValue.put(MediaStore.MediaColumns.DISPLAY_NAME,"Image_$count" +".png")
//                    contentValue.put(MediaStore.MediaColumns.MIME_TYPE,"image/png")
//                    contentValue.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder")
//                    println(contentValue)
//                    var uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValue)
//                    println(uri)
//                    var OutputStream : OutputStream? = applicationContext.contentResolver.openOutputStream(uri!!)
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,OutputStream)
                    val byte = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,byte)
                    val uri = Uri.parse(MediaStore.Images.Media.insertImage(resolver, bitmap, "Title", null))
                    println(uri)
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,
                           uri.toString())
                        .build())
                }
                if (cursor.getString(4) != null) {
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY,
                            cursor.getString(4))
                        .build())
                }
                if (cursor.getString(5) != null) {
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS,
                            cursor.getString(5))
                        .build())
                }
                if (cursor.getString(6) != null) {
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Relation.NAME,
                            cursor.getString(6))
                        .build())
                }
                if (cursor.getString(7) != null) {
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS,
                            cursor.getString(7))
                        .build())
                }
                if (cursor.getString(8) != null) {
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Nickname.NAME,
                            cursor.getString(8))
                        .build())
                }
                if (cursor.getString(9) != null) {
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Website.DATA,
                            cursor.getString(9))
                        .build())
                }
                if (cursor.getString(10) != null) {
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Note.NOTE,
                            cursor.getString(10))
                        .build())
                }
                context.contentResolver?.applyBatch(ContactsContract.AUTHORITY, contactList)
                count++
            }
        }

    }

}