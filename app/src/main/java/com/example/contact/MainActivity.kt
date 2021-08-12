 package com.example.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

 class MainActivity : AppCompatActivity() {
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var signinUser = intent.getStringExtra("email").toString()
           var listfrag = ContactListViewFragment()
           var bundle = Bundle()
           bundle.putString("email", signinUser)
           listfrag.arguments = bundle
           replaceFragment(listfrag)
           homenavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
               override fun onNavigationItemSelected(item: MenuItem): Boolean {
                   when(item.itemId){
                       R.id.homelist  -> {
                           var listfrag = ContactListViewFragment()
                           var bundle = Bundle()
                           bundle.putString("email", signinUser)
                           listfrag.arguments = bundle
                           replaceFragment(listfrag)
                       }
                       R.id.settings -> {
                           Toast.makeText(applicationContext,"Setting",Toast.LENGTH_SHORT).show()
                           replaceFragment(SettingFragment())
                       }
                   }
                   return true
               }
           })

       }
     fun replaceFragment(fragment : Fragment){
         val fragmentManager = supportFragmentManager
         val fragmentTransaction = fragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.mainfragment,fragment)
         fragmentTransaction.commit()
     }
}

