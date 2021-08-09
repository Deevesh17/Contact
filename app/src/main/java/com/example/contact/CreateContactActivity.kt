package com.example.contact

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.view.setPadding
import com.example.contact.model.DBHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.additional_field.*
import kotlinx.android.synthetic.main.additional_field.view.*
import kotlinx.android.synthetic.main.chooseimage.*
import kotlinx.android.synthetic.main.fieldtext.*
import kotlinx.android.synthetic.main.fieldtext.view.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

class CreateContactActivity : AppCompatActivity() {
    private var contactId = 0
    private val contactDb = DBHelper(this)
    private var base64image : String? = null
    override fun onResume() {
        super.onResume()
        val group = resources.getStringArray(R.array.group)
        val groupadapter = ArrayAdapter(this,R.layout.dropdown,group)
        selectgroup.setAdapter(groupadapter)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contact)
        contactId = intent.getIntExtra("contactid", -1)
        if (contactId != -1){
            topAppBarCreate.title = "Update Contact"
        }
        topAppBarCreate.setNavigationOnClickListener {
            onBackPressed()
        }
        addfield.setOnClickListener {
            bottomItemClicked()
        }
        topAppBarCreate.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        profileimagecreate.setOnClickListener{
           imagebottomitem()
        }
        if(contactId != -1){
            val cursor: Cursor? = contactDb.getDetailedData(contactId)
            if (cursor?.count != 0) {
                while (cursor?.moveToNext()!!) {
                    personName.setText(cursor.getString(1))
                    if(cursor.getString(2) != null){
                        val splitedNumber = cursor.getString(2).split(',')
                        var numbercount = 0
                        if(splitedNumber.isNotEmpty()){
                            for(numb in splitedNumber){
                                println(numb)
                                if (numbercount == 0) {
                                    personNumber.setText(numb)
                                    numbercount++
                                }
                                else {
                                    val view = layoutInflater.inflate(R.layout.fieldtext, null)
                                    view.additionalhint.hint = "Mobile"
                                    view.additionaltext.setText(numb)
                                    addAdditionalField(view)
                                }
                            }
                        }
                    }
                    if (cursor.getString(3) != null) {
                        base64image = cursor.getString(3)
                        val comressed = Base64.decode(cursor.getString(3), Base64.DEFAULT)
                        profileimagecreate.setImageBitmap(BitmapFactory.decodeByteArray(comressed,
                            0,
                            comressed.size))
                    }
                    if (cursor.getString(5) != null) email.setText(cursor.getString(5))
                    if (cursor.getString(4) != null) company.setText(cursor.getString(4))
                    if (cursor.getString(6) != null) selectgroup.setText(cursor.getString(6))
                    if (cursor.getString(7) != null) {
                        val view = layoutInflater.inflate(R.layout.fieldtext,null)
                        view.additionalhint.hint = "Address"
                        view.additionaltext.setText(cursor.getString(7))
                        addAdditionalField(view)
                    }
                   if (cursor.getString(9) != null) {
                        val view = layoutInflater.inflate(R.layout.fieldtext,null)
                        view.additionalhint.hint = "Website"
                        view.additionaltext.setText(cursor.getString(9))
                        addAdditionalField(view)
                    }
                    if (cursor.getString(10) != null) {
                        val view = layoutInflater.inflate(R.layout.fieldtext,null)
                        view.additionaltext.setPadding(80)
                        view.additionalhint.hint = "Notes"
                        view.additionaltext.setText(cursor.getString(10))
                        addAdditionalField(view)
                    }
                    if (cursor.getString(8) != null){
                        val view = layoutInflater.inflate(R.layout.fieldtext,null)
                        view.additionalhint.hint = "NickName"
                        view.additionaltext.setText(cursor.getString(8))
                        addAdditionalField(view)
                    }
                }
            }
        }
    }
    private fun getImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(Intent.createChooser(intent,"Select Image"),102)
    }
    private fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null){
            startActivityForResult(intent,103)
        }
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
        if(requestCode == 104 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePicture()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 102 && resultCode == RESULT_OK && data != null){
            val uri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                profileimagecreate.setImageBitmap(bitmap)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream)
                val byte = stream.toByteArray()
                base64image = Base64.encodeToString(byte,Base64.DEFAULT)
            }catch (e : Exception){
                println(e)
            }
        }
        if(requestCode == 103 && resultCode == RESULT_OK && data != null){
            val bundle = data.extras
            try {
                val bitmap = bundle?.get("data") as Bitmap
                profileimagecreate.setImageBitmap(bitmap)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream)
                val byte = stream.toByteArray()
                base64image = Base64.encodeToString(byte,Base64.DEFAULT)
            }catch (e : Exception){
                println(e)
            }
        }
    }
    private fun addAdditionalField(view :View){
        dynamicfield.addView(view)
    }
    private fun imagebottomitem(){
        val cooseimage = Dialog(this)
        cooseimage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        cooseimage.setContentView(R.layout.chooseimage)
        cooseimage.galary.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                cooseimage.dismiss()
                getImage()
            }
            else{
                cooseimage.dismiss()
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),101)
            }
        }
        cooseimage.camera.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                cooseimage.dismiss()
                takePicture()
            }
            else{
                cooseimage.dismiss()
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),104)
            }
        }
        cooseimage.show()
        cooseimage.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        cooseimage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cooseimage.window?.setGravity(Gravity.CENTER)
    }
    private fun bottomItemClicked(){
        val bottomsheet = BottomSheetDialog(this@CreateContactActivity,R.style.BottomSheetDialogTheam)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.additional_field,fieldslist)
        bottomSheetView.findViewById<View>(R.id.address).setOnClickListener{
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "Address"
            addAdditionalField(view)
        }
        bottomSheetView.findViewById<View>(R.id.nickname).setOnClickListener{
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "NickName"
            addAdditionalField(view)
        }
        bottomSheetView.findViewById<View>(R.id.notes).setOnClickListener{
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "Notes"
            view.additionaltext.setPadding(80)
            addAdditionalField(view)
        }
        bottomSheetView.findViewById<View>(R.id.website).setOnClickListener{
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "Website"
            addAdditionalField(view)
        }
        bottomSheetView.findViewById<View>(R.id.aditionalnumber).setOnClickListener{
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "Mobile"
            addAdditionalField(view)
        }
        bottomsheet.setContentView(bottomSheetView)
        bottomsheet.show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menudata,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
           R.id.save ->{
               val childcnt = dynamicfield.childCount
               var address :String?= null
               var nickname:String? = null
               var website :String? = null
               var notes :String? = null
               var mobile :String = personNumber.text.toString()
               for(index in 0..childcnt){
                   if(dynamicfield.getChildAt(index) != null){
                       when(dynamicfield.getChildAt(index).additionalhint.hint){
                           "Address" -> address = dynamicfield.getChildAt(index).additionaltext.text.toString()
                           "Notes" -> notes = dynamicfield.getChildAt(index).additionaltext.text.toString()
                           "NickName" -> nickname = dynamicfield.getChildAt(index).additionaltext.text.toString()
                           "Website" -> website = dynamicfield.getChildAt(index).additionaltext.text.toString()
                           "Mobile" -> {
                               mobile += ",${dynamicfield.getChildAt(index).additionaltext.text.toString()}"
                           }
                       }
                   }
               }
               if(contactId == -1 ) {
                   val result: Boolean = contactDb.insertuserdata(personName.text.toString(),
                       mobile,
                       base64image,
                       company.text.toString(),
                       email.text.toString(),
                       selectgroup.text.toString(),
                       address,
                       nickname,
                       website,
                       notes)
                   if (result) {
                       onBackPressed()
                   }
               }else{
                   val result : Boolean = contactDb.updateuserdata(
                       personName.text.toString(),
                       mobile,
                       company.text.toString(),
                       email.text.toString(),
                       selectgroup.text.toString(),
                       address,
                       nickname,
                       website,
                       notes,
                       contactId,
                       base64image
                   )
                   if(result){
                       onBackPressed()
                   }
               }
           }
        }
        return true
    }
}