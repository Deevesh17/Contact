 package com.example.contact.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.fragment.ContactListViewFragment
import kotlinx.android.synthetic.main.activity_main.*

 class MainActivity : AppCompatActivity() {
     lateinit var sharedPreferences: SharedPreferences
     lateinit var toogle : ActionBarDrawerToggle
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)


         sharedPreferences = this.getSharedPreferences(
             "com.example.contact.user",
             Context.MODE_PRIVATE
         )

         toogle = ActionBarDrawerToggle(this,maindrawable,R.string.open,R.string.close)
         maindrawable.addDrawerListener(toogle)
         toogle.syncState()
         supportActionBar?.setDisplayHomeAsUpEnabled(true)
         sidenav.setNavigationItemSelectedListener {
             when(it.itemId){
                 R.id.aboutnav -> Toast.makeText(this,"hai",Toast.LENGTH_SHORT).show()
             }
             true
         }
         println(toogle)
         val listfrag = ContactListViewFragment()
         replaceFragment(listfrag)
     }
     fun replaceFragment(fragment : Fragment){
         val fragmentManager = supportFragmentManager
         val fragmentTransaction = fragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.mainfragment,fragment)
         fragmentTransaction.commit()
     }
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         val fragdata = supportFragmentManager.findFragmentById(R.id.mainfragment)
         fragdata?.onActivityResult(requestCode,resultCode,data)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         if (toogle.onOptionsItemSelected(item)){
             return true
         }
         return super.onOptionsItemSelected(item)
     }

}

