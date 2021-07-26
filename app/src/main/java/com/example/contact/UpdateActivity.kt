package com.example.contact

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.example.contact.model.DBHelper
import kotlinx.android.synthetic.main.activity_create_contact.*
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity() {
    var mobilenumber :String? = ""
    val contactDb = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        topAppBarUpdate.setNavigationOnClickListener {
            onBackPressed()
        }
        topAppBarUpdate.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        person_Name.setText(intent.getStringExtra("personName"))
        person_umber.setText(intent.getStringExtra("personNumber"))
        mobilenumber = intent.getStringExtra("personNumber")
        company_name.setText(intent.getStringExtra("company"))
        emailid.setText(intent.getStringExtra("email"))
        noteup.setText(intent.getStringExtra("notes"))
        groupup.setText(intent.getStringExtra("group"))
        addressUp.setText(intent.getStringExtra("address"))
        site.setText(intent.getStringExtra("website"))
        nick.setText(intent.getStringExtra("nickname"))
    }
    override fun onResume() {
        super.onResume()
        val group = resources.getStringArray(R.array.group)
        val groupadapter = ArrayAdapter(this,R.layout.dropdown,group)
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
                    emailid.text.toString(),groupup.text.toString(),addressUp.text.toString(),nick.text.toString(),site.text.toString(),noteup.text.toString(),mobilenumber)
                if(result == true){
                    onBackPressed()
                }
            }
        }
        return true
    }
}