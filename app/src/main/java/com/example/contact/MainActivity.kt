package com.example.contact

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_actionbar.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var contactList : ArrayList<ContactData> = ArrayList<ContactData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        var layout = LinearLayoutManager(this)
        listview.layoutManager = layout
        if(!getPermissionAndList()){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),20)
        }
        listview.adapter = ContactAdapter(contactList)
    }
    fun getPermissionAndList() : Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val contact = contentResolver.query(
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
                    if (personprofile != null) {
                        profile = MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            Uri.parse(personprofile)
                        )
                    }
                    contactList.add(ContactData(personName, personNumber, profile))
                }
                contact.close()
            }
            return true
        }
        return false
    }

}