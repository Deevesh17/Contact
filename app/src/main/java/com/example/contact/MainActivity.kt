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
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.contact.model.ContactData
import com.example.contact.model.DBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progreesbar.*
import java.lang.Exception

 class MainActivity : AppCompatActivity() {
    var contactList :ArrayList<ContactData> = ArrayList<ContactData>()
    val contactDb = DBHelper(this)
     lateinit var dialog : Dialog
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
        create.setOnClickListener(){
            var intent = Intent(this,CreateContactActivity::class.java)
            startActivity(intent)
        }
         if(!(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)){
           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),55)
        }

    }
     override fun onResume() {
         super.onResume()
         contactList.clear()
         ContactTask().execute()
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
                        createWorker()
                    }else{
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),56)
                    }
                }catch (e : Exception){
                    println(e)
                }

            }
            R.id.exportfile -> Toast.makeText(this,"Export",Toast.LENGTH_SHORT).show()
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
                 createWorker()
             }
         }
         if(requestCode == 55){
             if(!(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)){
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
     fun createWorker(){
         dialog.importdetails.setText("Importing...")
         dialog.setCancelable(false)
         dialog.show()
         var workManager = WorkManager.getInstance(applicationContext)
         val import = OneTimeWorkRequest.Builder(ImportWorker::class.java).build()
         workManager.enqueue(import)
         workManager.getWorkInfoByIdLiveData(import.id).observe(this, Observer {
             if(it.state.name == "SUCCEEDED"){
                 contactList.clear()
                 ContactTask().execute()
             }
         })
     }
    fun createAdapter(contact : ArrayList<ContactData>)  {
        listview.adapter = ContactAdapter(contact)
    }
    inner class ContactTask : AsyncTask<Void, Int, Void>() {
        var cursorTotal = 0
        override fun doInBackground(vararg params: Void?): Void? {
            var cursor: Cursor? = contactDb.getdata()
            var count = 0
            cursorTotal = cursor?.count!!
            try{
            if (cursorTotal != 0) {
                while (cursor?.moveToNext()) {
                    if (cursor.getString(3) != null) {
                        var comressed = Base64.decode(cursor.getString(3), Base64.DEFAULT)
                        contactList.add(
                            ContactData(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                BitmapFactory.decodeByteArray(comressed, 0, comressed.size),
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
            }catch (e :Exception){
                println(e)
            }
           return null
        }
        override fun onProgressUpdate(vararg values: Int?) {
           if(contactList.size % 10 == 0){
               createAdapter(contactList)
           }
           dialog.importdetails.setText("Importing Contact ${values[0]} of $cursorTotal")
        }
        override fun onPostExecute(result: Void?) {
            createAdapter(contactList)
            dialog.dismiss()
        }
    }
}