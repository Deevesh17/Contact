 package com.example.contact.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.fragment.ContactListViewFragment
import com.example.contact.fragment.SettingFragment
import com.example.contact.fragment.WeatherFragment
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_main.*

 class MainActivity : AppCompatActivity() {
     var signinUser = ""
     val bundle = Bundle()
     override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_main)
           val acct = GoogleSignIn.getLastSignedInAccount(this)
           if (acct != null) {
               signinUser = acct.email
           } else if (AccessToken.isCurrentAccessTokenActive()) {
               returnEmailFacebook()
           } else {
               signinUser = intent.getStringExtra("email").toString()
           }
           val listfrag = ContactListViewFragment()
           bundle.putString("email", signinUser)
           listfrag.arguments = bundle
           replaceFragment(listfrag)
           homenavigation.setOnNavigationItemSelectedListener { item ->
               when (item.itemId) {
                   R.id.homelist -> {
                       val listfrag = ContactListViewFragment()
                       println(signinUser)
                       bundle.putString("email", signinUser)
                       listfrag.arguments = bundle
                       replaceFragment(listfrag)
                   }
                   R.id.settings -> {
                       val settingFragment = SettingFragment()
                       bundle.putString("email", signinUser)
                       settingFragment.arguments = bundle
                       replaceFragment(settingFragment)
                   }
                   R.id.weather ->{
                       val weather = WeatherFragment()
                       bundle.putString("email", signinUser)
                       weather.arguments = bundle
                       replaceFragment(weather)
                   }
               }
               true
           }
     }
     fun replaceFragment(fragment : Fragment){
         val fragmentManager = supportFragmentManager
         val fragmentTransaction = fragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.mainfragment,fragment)
         fragmentTransaction.commit()
     }
     fun returnEmailFacebook() {
         val request = GraphRequest.newMeRequest(
             AccessToken.getCurrentAccessToken()
         ) { `object`, response ->
             try {
                signinUser = `object`.getString("email")
             }
             catch (e : Exception){
                 println(e)
             }
         }
         val parameters = Bundle()
         parameters.putString("fields", "id,email")
         request.parameters = parameters
         request.executeAsync()
     }

}

