 package com.example.contact.activity

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navheader.view.*

 class MainActivity : AppCompatActivity() {
     lateinit var sharedPreferences: SharedPreferences
     lateinit var navControler : NavController
     lateinit var appBarConfiguration: AppBarConfiguration
     lateinit var broadcastReceiver: BroadcastReceiver
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         sharedPreferences = this.getSharedPreferences(
             "com.example.contact.user",
             Context.MODE_PRIVATE
         )
         val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainfragment) as NavHostFragment
         navControler = navHostFragment.navController


         appBarConfiguration = AppBarConfiguration(setOf(R.id.navhome,R.id.navaudio,R.id.navsettings),maindrawable)

         topAppBarmain.setupWithNavController(navControler,appBarConfiguration)
         sidenav.setupWithNavController(navControler)

         val viewModel = ContactViewModel(this)

         var userName = ""
         sharedPreferences.getString("email","")?.let { viewModel.setAboutUser(it) }
         viewModel.SaveResult.observe(this,  {
             if(it!= null && it != "")
             {
                 val result = it.split(",")
                 userName = result[0]
             }
         })
         sidenav.setNavigationItemSelectedListener {
             maindrawable.closeDrawer(GravityCompat.START)
             val bundle = Bundle()
             bundle.putString("userName",userName)
             when(it.itemId){
                 R.id.navhome->{
                     navControler.navigate(it.itemId)
                 }
                 R.id.navaudio->{
                     try{
//                         val action = ContactListViewFragmentDirections.actionNavhomeToAudioFragment("iamchanged")
////                         action.actionId
                         navControler.navigate(it.itemId,bundle)
                     }
                     catch (e : Exception){
                         navControler.navigate(it.itemId)
                     }
                 }
                 R.id.navsettings->{
                     navControler.navigate(it.itemId)
                 }

             }
             true
         }
     }

     override fun onStart() {
         super.onStart()
         val headerView = sidenav.getHeaderView(0)
         broadcastReceiver = object : BroadcastReceiver(){
             override fun onReceive(context: Context?, intent: Intent?) {
                 val count = intent?.extras
                 headerView.audiocount.text = "Audio List Available is ${count?.getInt("audioCount")}"
             }
         }
         val intentFilter = IntentFilter("update.audio.count")
         registerReceiver(broadcastReceiver,intentFilter)

     }
     override fun onStop() {
         super.onStop()
         unregisterReceiver(broadcastReceiver)
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

     override fun onSupportNavigateUp(): Boolean {
         val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainfragment) as NavHostFragment
         val navController = navHostFragment.navController
         return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
     }



 }

