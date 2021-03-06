package com.example.contact.activity

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.contact.R
import com.example.contact.adapter.ContactAdapter
import com.example.contact.model.ContactData
import com.example.contact.model.DBHelper
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_select_contact.*
import kotlinx.android.synthetic.main.progreesbar.*

class SelectContactActivity : AppCompatActivity() {
    lateinit var dialog : Dialog
    private lateinit var contactAdapter : ContactAdapter
    var selectedList :ArrayList<ContactData> = ArrayList()
    val contactDb = DBHelper(this)
    var signinUser = ""
    val title = ContactViewModel(this)
    var contactList :ArrayList<ContactData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_contact)

        signinUser = intent.getStringExtra("email").toString()

        topAppBarSelect.setNavigationOnClickListener {
            onBackPressed()
        }
        topAppBarSelect.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

//        progressbar
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progreesbar)
        dialog.importdetails.setText("Importing...")
        dialog.setCancelable(false)
        dialog.show()

//        observer the import state from the local contact
        title.setImportData(title,"Import",selectedList,signinUser)
        title.contactList.observe(this, Observer {
            dialog.dismiss()
            contactList = it
            createAdapter(it)
        })

//        observed the saved state
        title.SaveResult.observe(this, Observer {
            onBackPressed()
        })


//        search bar
        importsearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contactAdapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })

//        observing the State for selected count
        title.contactDataLive.observe(this, Observer {
            if(contactAdapter.selectedCount > 0){
                topAppBarSelect.title = it
            }
            else{
                topAppBarSelect.title = "Select Contact"
                dialog.dismiss()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menudata,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.selectcontact ->{
                if(contactAdapter.isSelectAll){
                    selectedList = contactList
                }else{
                    selectedList = contactAdapter.getSelectedContactList()
                }
                dialog.importdetails.setText("Saving...")
                dialog.setCancelable(false)
                dialog.show()
                title.setImportData(title,"Save",selectedList,signinUser)
            }
            R.id.selectAll ->{
                contactAdapter.isSelectAll =  !contactAdapter.isSelectAll
                if(!contactAdapter.isSelectAll){
                    contactAdapter.selectedPosition.clear()
                    contactAdapter.selectedCount = 0
                    contactAdapter.removedPosition.clear()
                    title.setValutodata("Selected(${contactAdapter.selectedCount}/${contactList.size})")
                }else  {
                    contactAdapter.selectedCount = contactList.size
                    title.setValutodata("Selected(${contactAdapter.selectedCount}/${contactList.size})")
                }
                contactAdapter.notifyDataSetChanged()
            }

        }
        return true
    }
    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }
    private fun createAdapter(contactList: ArrayList<ContactData>) {
        contactAdapter = ContactAdapter(contactList,title,signinUser)
        selectList.adapter = contactAdapter
    }
}