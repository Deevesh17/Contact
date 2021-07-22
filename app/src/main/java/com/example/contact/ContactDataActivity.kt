package com.example.contact

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_actionbar.*
import kotlinx.android.synthetic.main.activity_contact_data.*

class ContactDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_data)
        topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        personname.setText(intent.getStringExtra("personName"))
        mobilenumber.setText(intent.getStringExtra("personNumber"))
        if(intent.getStringExtra("profile") != null){
            var comressed  = Base64.decode(intent.getStringExtra("profile"),Base64.DEFAULT)
            personpic.setImageBitmap(BitmapFactory.decodeByteArray(comressed,0,comressed.size))
        }
    }
}