 package com.example.contact

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.contact.model.ContactData
import com.example.contact.model.DBHelper
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_contact_data.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progreesbar.*
import java.lang.Exception
import kotlin.collections.ArrayList

 class MainActivity : AppCompatActivity() {
    var contactList :ArrayList<ContactData> = ArrayList()
    val contactDb = DBHelper(this)
    lateinit var dialog : Dialog
    private lateinit var contactAdapter :ContactAdapter
     val title = ContactViewModel(this)
     var selectedList :ArrayList<ContactData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progreesbar)
        topAppBarmain.setNavigationOnClickListener {
            onBackPressed()
        }
        topAppBarmain.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        create.setOnClickListener{
            val intent = Intent(this,CreateContactActivity::class.java)
            startActivity(intent)
        }
         if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),55)
        }
        title.contactDataLive.observe(this, Observer {
            if (contactAdapter.selectedCount > 0){
                topAppBarmain.title = it
                topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = true
                topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
                topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = true
                topAppBarmain.menu.findItem(R.id.exportfile).isVisible = true

            }else{
                topAppBarmain.title = "Contact"
                topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
                topAppBarmain.menu.findItem(R.id.importfile).isVisible = true
                topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
                topAppBarmain.menu.findItem(R.id.exportfile).isVisible = false
            }
        })

    }
     override fun onResume() {
         super.onResume()
         contactList.clear()
         ContactTask().execute("GetDB")
     }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menudata,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.importfile -> {
                try{
                    if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
//                        createWorker()
                        val intent = Intent(this,SelectContactActivity::class.java)
                        startActivity(intent)
                    }else{
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),56)
                    }
                }catch (e : Exception){
                    println(e)
                }

            }
            R.id.exportfile ->{
                try{
                    if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        writeContact()
                    }else{
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE),25)
                    }
                }catch (e : Exception){
                    println(e)
                }
            }
            R.id.Deletefilemain ->{
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Contacts")
                    .setMessage("Delete the Selected contacts info from this device?")
                    .setNeutralButton("Cancel") { dialog, which ->
                    }
                    .setPositiveButton("Delete") { dialog, which ->
                        if(contactAdapter.isSelectAll){
                            println(contactAdapter.isSelectAll)
                            selectedList = ArrayList<ContactData>(contactList)
                        }else{
                            selectedList = contactAdapter.getSelectedContactList()
                        }
                        if(contactAdapter.selectedCount > 0){
                            contactAdapter.selectedPosition.clear()
                            contactAdapter.selectedCount = 0
                            contactAdapter.removedPosition.clear()
                            title.setValutodata("Selected(${contactAdapter.selectedCount}/${contactList.size})")
                        }
                        ContactTask().execute("Delete")
                        contactList.clear()
                        ContactTask().execute("GetDB")

                    }
                    .show()

            }
            R.id.selectAllmain ->{
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
            R.id.searchitem ->{
                val search = topAppBarmain.menu.findItem(R.id.searchitem)
                val searchView = search?.actionView as SearchView
                searchView.imeOptions = EditorInfo.IME_ACTION_DONE
                searchView.queryHint = "Search Contact"
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        contactAdapter.filter.filter(newText)
                        contactAdapter.notifyDataSetChanged()
                        return false
                    }
                })
            }
        }
        return true
    }
     override fun onRequestPermissionsResult(
         requestCode: Int,
         permissions: Array<out String>,
         grantResults: IntArray
     ) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         if(requestCode == 56){
             if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                 val intent = Intent(this,SelectContactActivity::class.java)
                 startActivity(intent)
             }
         }
         if(requestCode == 25){
             if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                 writeContact()
             }
         }
         if(requestCode == 55){
             if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                 Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
             }
             else{
                 Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
             }
         }
     }
     override fun onPause() {
         super.onPause()
         dialog.dismiss()
     }
     private fun writeContact(){
         Toast.makeText(this,"Export",Toast.LENGTH_SHORT).show()
         val workManager = WorkManager.getInstance(applicationContext)
         val import = OneTimeWorkRequest.Builder(ExportContact::class.java).build()
         workManager.enqueue(import)
     }
//     fun createWorker(){
//         dialog.importdetails.setText("Importing...")
//         dialog.setCancelable(false)
//         dialog.show()
//         var workManager = WorkManager.getInstance(applicationContext)
//         val import = OneTimeWorkRequest.Builder(ImportWorker::class.java).build()
//         workManager.enqueue(import)
//         workManager.getWorkInfoByIdLiveData(import.id).observe(this, Observer {
//             if(it.state.name == "SUCCEEDED"){
//                 contactList.clear()
//                 ContactTask().execute()
//             }
//         })
//     }
    fun createAdapter(contact : ArrayList<ContactData>)  {
      contactAdapter = ContactAdapter(contact, title)
      listview.adapter = contactAdapter
}
    inner class ContactTask : AsyncTask<String, Int, Void>() {
        private var cursorTotal = 0
        var params = ""
        override fun doInBackground(vararg params: String?): Void? {
            this.params = params[0].toString()
            if (params[0] == "GetDB") {
                val cursor: Cursor? = contactDb.getdata()
                var count = 0
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
                            publishProgress(count)
                            count++

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
        override fun onProgressUpdate(vararg values: Int?) {
           if(contactList.size % 10 == 0){
               createAdapter(contactList)
           }
           dialog.importdetails.setText("Importing Contact ${values[0]} of $cursorTotal")
        }

        override fun onPreExecute() {
            super.onPreExecute()
            if(params == "Delete") {
                dialog.importdetails.setText("Deleting...")
                dialog.setCancelable(false)
                dialog.show()
            }
        }
        override fun onPostExecute(result: Void?) {
            if (params == "GetDB") {
                createAdapter(contactList)
            }
            dialog.dismiss()
        }
    }
}

