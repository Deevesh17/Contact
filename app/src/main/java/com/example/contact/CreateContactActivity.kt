package com.example.contact

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.setPadding
import com.example.contact.model.DBHelper
import com.example.contact.model.ImportData
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.additional_field.*
import kotlinx.android.synthetic.main.additional_field.view.*
import kotlinx.android.synthetic.main.fieldtext.*
import kotlinx.android.synthetic.main.fieldtext.view.*

class CreateContactActivity : AppCompatActivity() {
    val contactDb = DBHelper(this)
    override fun onResume() {
        super.onResume()
        val group = resources.getStringArray(R.array.group)
        val groupadapter = ArrayAdapter(this,R.layout.dropdown,group)
        selectgroup.setAdapter(groupadapter)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contact)
        topAppBarCreate.setNavigationOnClickListener {
            onBackPressed()
        }
        addfield.setOnClickListener {
            bottomItemClicked()
        }
        topAppBarCreate.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

    private fun addAdditionalField(view :View){
        dynamicfield.addView(view)
    }

    private fun bottomItemClicked(){
        val bottomsheet = BottomSheetDialog(this@CreateContactActivity,R.style.BottomSheetDialogTheam)
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.additional_field,fieldslist as LinearLayout?)
        bottomSheetView.findViewById<View>(R.id.address).setOnClickListener(){
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "Address"
            addAdditionalField(view)
        }
        bottomSheetView.findViewById<View>(R.id.nickname).setOnClickListener(){
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "NickName"
            addAdditionalField(view)
        }
        bottomSheetView.findViewById<View>(R.id.notes).setOnClickListener(){
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "Notes"
            view.additionaltext.setPadding(80)
            addAdditionalField(view)
        }
        bottomSheetView.findViewById<View>(R.id.website).setOnClickListener(){
            bottomsheet.dismiss()
            val view = layoutInflater.inflate(R.layout.fieldtext,null)
            view.additionalhint.hint = "Website"
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
               var childcnt = dynamicfield.childCount
               var address :String?= null
               var nickname:String? = null
               var website :String? = null
               var notes :String? = null
               for(index in 0..childcnt){
                   if(dynamicfield.getChildAt(index) != null){
                       when(dynamicfield.getChildAt(index).additionalhint.hint){
                           "Address" -> address = dynamicfield.getChildAt(index).additionaltext.text.toString()
                           "Notes" -> notes = dynamicfield.getChildAt(index).additionaltext.text.toString()
                           "NickName" -> nickname = dynamicfield.getChildAt(index).additionaltext.text.toString()
                           "Website" -> website = dynamicfield.getChildAt(index).additionaltext.text.toString()
                       }
                   }
               }
               var result : Boolean? = contactDb.insertuserdata(personName.text.toString(),personNumber.text.toString(),null,company.text.toString(),
                   email.text.toString(),selectgroup.text.toString(),address,nickname, website, notes)
               if(result == true){
                   onBackPressed()
               }
           }
        }
        return true
    }
}