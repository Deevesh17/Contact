package com.example.contact

import android.content.Intent
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
import com.example.contact.model.DBHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_actionbar.*
import kotlinx.android.synthetic.main.activity_contact_data.*
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.fieldtext.view.*
import java.io.ByteArrayOutputStream

class ContactDataActivity : AppCompatActivity() {
    val contactDb = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_data)
        topAppBaredit.setNavigationOnClickListener {
            onBackPressed()
        }
        topAppBaredit.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        personname.setText(intent.getStringExtra("personName"))
        mobilenumber.setText(intent.getStringExtra("personNumber"))
        if(intent.getStringExtra("profile") != null){
            var comressed  = Base64.decode(intent.getStringExtra("profile"),Base64.DEFAULT)
            personpic.setImageBitmap(BitmapFactory.decodeByteArray(comressed,0,comressed.size))
        }
        if(intent.getStringExtra("email") != null){
           emaildata.setText(intent.getStringExtra("email"))
        }
        if(intent.getStringExtra("address") != null){
            addressview.setText(intent.getStringExtra("address"))
        }
        if(intent.getStringExtra("company") != null){
            companyName.setText(intent.getStringExtra("company"))
        }
        if(intent.getStringExtra("group") != null){
            groupName.setText(intent.getStringExtra("group"))
        }
        if(intent.getStringExtra("website") != null){
            web.setText(intent.getStringExtra("website"))
        }
        if(intent.getStringExtra("notes") != null){
            Notes.setText(intent.getStringExtra("notes"))
        }
        if(intent.getStringExtra("nickname") != null){
            nickName.setText(intent.getStringExtra("nickname"))
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
                        val result : Boolean? =  contactDb.deletedata(mobilenumber.text.toString())
                        if(result == true ){
                            onBackPressed()
                        }
                    }
                    .show()

            }
            R.id.edit -> {
                var intent : Intent = Intent(this,UpdateActivity::class.java)
                intent.putExtra("personName",personname.text)
                intent.putExtra("personNumber",mobilenumber.text)
                intent.putExtra("company",companyName.text)
                intent.putExtra("email",emaildata.text)
                intent.putExtra("notes",Notes.text)
                intent.putExtra("group",groupName.text)
                intent.putExtra("address",addressview.text)
                intent.putExtra("website",web.text)
                intent.putExtra("nickname",nickName.text)

                startActivity(intent)
            }
        }
        return true
    }
}