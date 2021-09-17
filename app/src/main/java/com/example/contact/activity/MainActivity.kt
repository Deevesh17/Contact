 package com.example.contact.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.contact.R
import com.example.contact.viewmodel.ContactViewModel
import com.example.contact.worker.GoogleContactSyncWorker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progreesbar.*

 class MainActivity : AppCompatActivity() {
     lateinit var toogle : ActionBarDrawerToggle
     lateinit var progressDialog: Dialog
     lateinit var sharedPreferences: SharedPreferences
     lateinit var sharedPreferencesEditor : SharedPreferences.Editor
     lateinit var contactViewModel: ContactViewModel
     lateinit var navControler :NavController
     lateinit var appBarConfiguration: AppBarConfiguration
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         progressDialog = Dialog(this)
         progressDialog.setContentView(R.layout.progreesbar)

         sharedPreferences = this.getSharedPreferences(
             "com.example.contact.user",
             Context.MODE_PRIVATE
         )
         sharedPreferencesEditor = sharedPreferences.edit()

         val googleSignin = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN )
             .requestEmail()
             .build()
         val mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignin);

         supportActionBar?.setDisplayHomeAsUpEnabled(true)

         val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainfragment) as NavHostFragment
         navControler = navHostFragment.navController

         appBarConfiguration = AppBarConfiguration(setOf(R.id.navhome,R.id.navsetting),maindrawable)

         topAppBarmain.setupWithNavController(navControler,appBarConfiguration)
         sidenav.setupWithNavController(navControler)
         sidenav.setNavigationItemSelectedListener {
             maindrawable.closeDrawer(GravityCompat.START)
             it.onNavDestinationSelected(navControler)
             true
         }

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
         if (requestCode == 200){
             googleSyncWorker()
         }
     }
     private fun googleSyncWorker(){
         progressDialog.importdetails.text = "Contact Syncing..."
         progressDialog.setCancelable(false)
         progressDialog.show()
         val workManager = WorkManager.getInstance(this)
         val googleContactSyncWorker = OneTimeWorkRequest.Builder(GoogleContactSyncWorker::class.java).build()
         workManager.enqueue(googleContactSyncWorker)

         workManager.getWorkInfoByIdLiveData(googleContactSyncWorker.id).observe(this
             , androidx.lifecycle.Observer {
                 when (it.state.name) {
                     "SUCCEEDED" -> {
                         progressDialog.dismiss()
//                         val contactListViewFragment = ContactListViewFragment()
//                         replaceFragment(contactListViewFragment)
                     }
                 }
             })
     }

     override fun onSupportNavigateUp(): Boolean {
         val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainfragment) as NavHostFragment
         val navController = navHostFragment.navController
         return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         if (toogle.onOptionsItemSelected(item)){
             return true
         }
         return super.onOptionsItemSelected(item)
     }

     override fun onPause() {
         super.onPause()
         sharedPreferencesEditor.apply()
     }
}

