package com.example.contact

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.contact.model.ContactData
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_actionbar.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var contactViewModel : ContactViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        topAppBar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        contactViewModel = ContactViewModel(this)
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            contactViewModel.setContactList()
            contactViewModel.contactDataLive.observe(this, Observer {
                createAdapter(it)
            })

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),20)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menudata,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.importfile -> {
                Toast.makeText(this,"Import",Toast.LENGTH_SHORT).show()
            }
            R.id.exportfile -> Toast.makeText(this,"Export",Toast.LENGTH_SHORT).show()
        }
        return true
    }
    fun createAdapter(contact : ArrayList<ContactData>)  {
        listview.adapter = ContactAdapter(contact)
    }
}