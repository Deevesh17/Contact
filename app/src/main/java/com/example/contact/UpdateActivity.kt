package com.example.contact

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.contact.model.DBHelper
import kotlinx.android.synthetic.main.activity_contact_data.*
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.activity_update.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

class UpdateActivity : AppCompatActivity() {
    val contactDb = DBHelper(this)
    var contactId : Int = 0
    var base64image : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        topAppBarUpdate.setNavigationOnClickListener {
            onBackPressed()
        }
        topAppBarUpdate.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        picupdate.setOnClickListener(){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                getImage()
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),101)
            }
        }
        contactId = intent.getIntExtra("contactid", 0)
        var cursor: Cursor? = contactDb.getDetailedData(contactId)
        if (cursor?.count != 0) {
            while (cursor?.moveToNext()!!) {
                person_Name.setText(cursor?.getString(1))
                person_umber.setText(cursor?.getString(2))
                if (cursor?.getString(3) != null) {
                    base64image = cursor?.getString(3)
                    var comressed = Base64.decode(cursor?.getString(3), Base64.DEFAULT)
                    picupdate.setImageBitmap(BitmapFactory.decodeByteArray(comressed,
                        0,
                        comressed.size))
                }
                if (cursor?.getString(5) != null) emailid.setText(cursor?.getString(5))
                if (cursor?.getString(7) != null) addressUp.setText(cursor?.getString(7))
                if (cursor?.getString(4) != null) company_name.setText(cursor?.getString(4))
                if (cursor?.getString(6) != null) groupup.setText(cursor?.getString(6))
                if (cursor?.getString(9) != null) site.setText(cursor?.getString(9))
                if (cursor?.getString(10) != null) noteup.setText(cursor?.getString(10))
                if (cursor?.getString(8) != null) nick.setText(cursor?.getString(8))
            }
        }
    }
    fun getImage(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(Intent.createChooser(intent,"Select Image"),102)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 102 && resultCode == RESULT_OK && data != null){
            var uri = data.data
            try {
                var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                picupdate.setImageBitmap(bitmap)
                var stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream)
                var byte = stream.toByteArray()
                base64image = Base64.encodeToString(byte,Base64.DEFAULT)
            }catch (e : Exception){
                println(e)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val group = resources.getStringArray(R.array.group)
        val groupadapter = ArrayAdapter(this, R.layout.dropdown, group)
        groupup.setAdapter(groupadapter)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menudata,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save ->{
                var result : Boolean? = contactDb.updateuserdata(person_Name.text.toString(),person_umber.text.toString(),company_name.text.toString(),
                    emailid.text.toString(),groupup.text.toString(),addressUp.text.toString(),nick.text.toString(),site.text.toString(),noteup.text.toString(),contactId,base64image)
                if(result == true){
                    onBackPressed()
                }
            }
        }
        return true
    }
}