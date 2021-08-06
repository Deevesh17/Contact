package com.example.contact

import android.content.ContentProviderOperation
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contact.model.DBHelper
import java.lang.Exception

class ExportContact(var context: Context, var workerParams: WorkerParameters) : Worker(context, workerParams) {
    val contactDb = DBHelper(context)
    override fun doWork(): Result {
        export()
        return Result.success()
    }
    fun export(){
        var cursorTotal = 0
        var cursor: Cursor? = contactDb.getdata()
        var count = 0
        cursorTotal = cursor?.count!!
        try{
            if (cursorTotal != 0) {
                while (cursor?.moveToNext()) {
                    var contactList : ArrayList<ContentProviderOperation> = ArrayList()
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
                    contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,  cursor.getString(1)).build())
                    var numberlist = cursor.getString(2).split(",")
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
                    if(  cursor.getString(4) != null) {
                        contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY,
                                cursor.getString(4))
                            .build())
                    }
                    if( cursor.getString(5) != null) {
                        contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS,
                                cursor.getString(5))
                            .build())
                    }
                    if( cursor.getString(7) != null){
                        contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS,
                                cursor.getString(7))
                            .build())
                    }
                    if( cursor.getString(8) != null){
                        contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Nickname.NAME,
                                cursor.getString(8))
                            .build())
                    }
                    if( cursor.getString(9) != null){
                        contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Website.DATA,
                                cursor.getString(9))
                            .build())
                    }
                    if( cursor.getString(10) != null){
                        contactList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Note.NOTE,
                                cursor.getString(10))
                            .build())
                    }
                   context.contentResolver?.applyBatch(ContactsContract.AUTHORITY,contactList)
                }
            }
        }catch (e : Exception){
            println(e)
        }
    }

}