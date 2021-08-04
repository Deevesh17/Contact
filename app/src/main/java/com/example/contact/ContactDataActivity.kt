package com.example.contact

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.contact.model.DBHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_actionbar.*
import kotlinx.android.synthetic.main.activity_contact_data.*
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.fieldtext.view.*
import java.io.ByteArrayOutputStream

class ContactDataActivity : AppCompatActivity() {
    val contactDb = DBHelper(this)
    var contactId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_data)
        topAppBaredit.setNavigationOnClickListener {
            onBackPressed()
        }
        topAppBaredit.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }
    override fun onResume() {
        super.onResume()
        contactId = intent.getIntExtra("contactid",0)
        var cursor: Cursor? = contactDb.getDetailedData(contactId)
        if(cursor?.count != 0){
            while (cursor?.moveToNext()!!) {
                personname.setText(cursor?.getString(1))
                mobilenumber.setText(cursor?.getString(2))
                if(cursor?.getString(3) != null){
                    var comressed  = Base64.decode(cursor?.getString(3),Base64.DEFAULT)
                    personpic.setImageBitmap(BitmapFactory.decodeByteArray(comressed,0,comressed.size))
                }
                if(cursor?.getString(5) != null)  emaildata.setText(cursor?.getString(5))
                else {
                    emailicon.isVisible = false
                    emaildata.isVisible = false
                }
                if(cursor?.getString(7) != null) addressview.setText(cursor?.getString(7))
                else {
                    homeicon.isVisible = false
                    addressview.isVisible = false
                }
                if(cursor?.getString(4) != null) companyName.setText(cursor?.getString(4))
                else {
                    companyicon.isVisible = false
                    companyName.isVisible = false
                }
                if(cursor?.getString(6) != null) groupName.setText(cursor?.getString(6))
                else {
                    groupicon.isVisible = false
                    groupName.isVisible = false
                }
                if(cursor?.getString(9) != null) web.setText(cursor?.getString(9))
                else {
                    websiteicon.isVisible = false
                    web.isVisible = false
                }
                if(cursor?.getString(10) != null) Notes.setText(cursor?.getString(10))
                else {
                    noteicon.isVisible = false
                    Notes.isVisible = false
                }
                if(cursor?.getString(8) != null) nickName.setText(cursor?.getString(8))
                else {
                    nickicon.isVisible = false
                    nickName.isVisible = false
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menudata,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete ->{
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Contact")
                    .setMessage("Delete the contact info of ${personname.text} from this device?")
                    .setNeutralButton("Cancel") { dialog, which ->
                    }
                    .setPositiveButton("Delete") { dialog, which ->
                        val result : Boolean? =  contactDb.deletedata(contactId)
                        if(result == true ){
                            onBackPressed()
                        }
                    }
                    .show()

            }
            R.id.edit -> {
                var intent : Intent = Intent(this,UpdateActivity::class.java)
                intent.putExtra("contactid",contactId)
                startActivity(intent)
            }
        }
        return true
    }
}