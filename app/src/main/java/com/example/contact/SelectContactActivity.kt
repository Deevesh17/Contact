package com.example.contact

import android.app.Dialog
import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.example.contact.model.ContactData
import com.example.contact.model.DBHelper
import com.example.contact.model.ImportData
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_select_contact.*
import kotlinx.android.synthetic.main.progreesbar.*
import java.io.ByteArrayOutputStream

class SelectContactActivity : AppCompatActivity() {
    lateinit var dialog : Dialog
    private lateinit var contactAdapter :ContactAdapter
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
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progreesbar)
        ContactTask().execute()
        importsearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contactAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        title.contactDataLive.observe(this, Observer {
            if(contactAdapter.selectedCount > 0){
                topAppBarSelect.title = it
            }
            else{
                topAppBarSelect.title = "Select Contact"
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
                SaveToDb().execute()
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
    inner class ContactTask : AsyncTask<Void, Int, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            var importContact = ImportData(applicationContext)
            importContact.addContactData()
            contactList = importContact.getContacDatatList()
            return null
        }
        override fun onPreExecute() {
            super.onPreExecute()
            dialog.importdetails.setText("Importing...")
            dialog.setCancelable(false)
            dialog.show()
        }
        override fun onPostExecute(result: Void?) {
            createAdapter(contactList)
            dialog.dismiss()

        }
    }
    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }
    inner class SaveToDb : AsyncTask<Void, Int, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            if(selectedList.size > 0){
                for(contactData in selectedList){
                    var stream = ByteArrayOutputStream()
                    var base64image : String? = null
                    if( contactData.profile != null) {
                        contactData.profile?.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                        var byte = stream.toByteArray()
                        base64image = Base64.encodeToString(byte, Base64.DEFAULT)
                    }
                    contactDb.insertuserdata(contactData.name,contactData.number,base64image,contactData.company,contactData.email,contactData.contactgroup,contactData.address,contactData.nickname,contactData.website,contactData.notes,signinUser) }
            }
            return null
        }
        override fun onPostExecute(result: Void?) {
            dialog.dismiss()
            onBackPressed()
        }
    }
    private fun createAdapter(contactList: ArrayList<ContactData>) {
        contactAdapter = ContactAdapter(contactList,title,signinUser)
        selectList.adapter = contactAdapter
    }
}