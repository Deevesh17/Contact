package com.example.contact

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.contact.model.DBHelper
import kotlinx.android.synthetic.main.activity_actionbar.*
import kotlinx.android.synthetic.main.activity_contact_data.*
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.fieldtext.view.*

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
                val result : Boolean? =  contactDb.deletedata(mobilenumber.text.toString())
                if(result == true ){
                    onBackPressed()
                }
            }
        }
        return true
    }
}