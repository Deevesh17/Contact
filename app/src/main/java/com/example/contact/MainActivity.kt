 package com.example.contact

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.contact.model.ContactData
import com.example.contact.model.ImportData
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_actionbar.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var contactViewModel : ContactViewModel = ContactViewModel(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topAppBarmain.setNavigationOnClickListener {
            onBackPressed()
        }
        topAppBarmain.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        create.setOnClickListener(){
            var intent = Intent(this,CreateContactActivity::class.java)
            startActivity(intent)
        }
        if(!(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)){
           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),5)
        }
    }

    override fun onResume() {
        super.onResume()
        contactViewModel.setContactList()
        contactViewModel.contactDataLive.observe(this, Observer {
            createAdapter(it)
        })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menudata,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.importfile -> {
                if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                    ImportData(this).addContactData()
                   contactViewModel.setContactList()
                }
            }
            R.id.exportfile -> Toast.makeText(this,"Export",Toast.LENGTH_SHORT).show()
        }
        return true
    }
    fun createAdapter(contact : ArrayList<ContactData>)  {
        listview.adapter = ContactAdapter(contact)
    }
}