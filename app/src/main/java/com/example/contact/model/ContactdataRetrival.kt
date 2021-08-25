package com.example.contact.model

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Base64
import com.example.contact.viewmodel.ContactViewModel

class ContactdataRetrival( val listType : String,var selectedList :ArrayList<ContactData> ,val signinUser : String,ctx : Context,val title: ContactViewModel) {
    val contactDb : DBHelper = DBHelper(ctx)
    var contactList :ArrayList<ContactData> = ArrayList()
    inner class ContactTask : AsyncTask<String, Int, Void>() {
        private var cursorTotal = 0
        var params = ""
        override fun doInBackground(vararg params: String?): Void? {
            this.params = params[0].toString()
            if (params[0] == "GetDB") {
                val cursor: Cursor? = contactDb.getdata(signinUser)
                cursorTotal = cursor?.count!!
                try {
                    if (cursorTotal != 0) {
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
            }
            else if (params[0] == "Delete"){
                if(selectedList.size > 0){
                    for(contactData in selectedList){
                        contactDb.deletedata(contactData.contactId)
                    }
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            title.contactList.value = contactList
        }
    }

}