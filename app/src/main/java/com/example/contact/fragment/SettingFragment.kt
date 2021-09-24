package com.example.contact.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.contact.R
import com.example.contact.activity.UserActivity
import com.example.contact.model.RemoteConfigUtills
import com.example.contact.viewmodel.ContactViewModel
import com.example.contact.worker.DeleteContactWorker
import com.example.contact.worker.GoogleContactSyncWorker
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.api.services.people.v1.PeopleServiceScopes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import kotlinx.android.synthetic.main.progreesbar.*
import java.util.*

class SettingFragment : androidx.fragment.app.Fragment(R.layout.fragment_setting) {
    lateinit var progressDialog: Dialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferencesEditor : SharedPreferences.Editor
    lateinit var contactViewModel: ContactViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        contactViewModel = ContactViewModel(requireActivity())
        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()

        requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.exportfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.recent).isVisible = false
        requireActivity().topAppBarmain.title = "Setting"
        requireActivity().topAppBarmain.subtitle = ""
        requireActivity().topAppBarmain.setBackgroundColor(Color.parseColor(RemoteConfigUtills.getAudioToolBarBackground()))

        requireActivity().topAppBarmain.setNavigationIcon(R.drawable.ic_action_menu)

        val view = inflater.inflate(R.layout.fragment_setting,container,false)
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progreesbar)


        val googleSignin = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN )
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignin);

        requireActivity().topAppBarmain.setNavigationOnClickListener {
            requireActivity().maindrawable.openDrawer(GravityCompat.START)
        }
//        signout Progress
        view.outcard.setOnClickListener {
            when {
                GoogleSignIn.getLastSignedInAccount(requireActivity()) != null -> {
                    MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("SignOut!!")
                        .setMessage("Sure You Want to sign out from this device?")
                         .setNeutralButton("No") { dialog, which ->
                        }
                        .setPositiveButton("Yes") { dialog, which ->

                            mGoogleSignInClient.revokeAccess()
                            mGoogleSignInClient.signOut().addOnCompleteListener {

                                progressDialog.importdetails.setText("Signing Out...")
                                progressDialog.setCancelable(false)
                                progressDialog.show()

                                sharedPreferencesEditor.putString("email", "")
        //                            val db = DBHelper(requireContext())
        //                            db.dropDatabase()

                                val workManager = WorkManager.getInstance(requireContext())
                                val deleteContact = OneTimeWorkRequest.Builder(DeleteContactWorker::class.java).build()
                                workManager.enqueue(deleteContact)
                                workManager.getWorkInfoByIdLiveData(deleteContact.id).observe(requireActivity(), androidx.lifecycle.Observer {

                                    when (it.state.name) {
                                        "SUCCEEDED" -> {
                                            val intent =
                                                Intent(requireActivity(), UserActivity::class.java)
                                            progressDialog.dismiss()
                                            startActivity(intent)
                                            requireActivity().finish()
                                        }
                                    }
                                })


                            }
                        }
                        .show()
                }
                AccessToken.isCurrentAccessTokenActive() -> {
                    MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("SignOut!!")
                        .setMessage("Sure You Want to sign out from this device?")
                        .setNeutralButton("No") { dialog, which ->
                        }
                        .setPositiveButton("Yes") { dialog, which ->
                            progressDialog.importdetails.setText("Signing Out...")
                            progressDialog.setCancelable(false)
                            progressDialog.show()
                            val intent =
                                Intent(requireActivity(), UserActivity::class.java)
                            Timer().schedule(object : TimerTask() {
                                override fun run() {
                                    progressDialog.dismiss()
                                    Firebase.auth.signOut()
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            }, 3000)
                        }
                        .show()
                }
                else -> {
                    sharedPreferencesEditor.putBoolean("isUserLoggedIn", false)
                    sharedPreferencesEditor.putString("email", "")
                    val intent =
                        Intent(requireActivity(), UserActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                        when (key) {
                            "isUserLoggedIn" -> {
                                if(!sharedPreferences.getBoolean("isUserLoggedIn",false)) {

                                    val workManager = WorkManager.getInstance(requireContext())
                                    val deleteContact =
                                        OneTimeWorkRequest.Builder(DeleteContactWorker::class.java)
                                            .build()
                                    workManager.enqueue(deleteContact)
                                }
                            }
                        }

                    }

                }
            }
        }

//        change password
        view.changepasscard.setOnClickListener{
            val changeFragment = ChangePasswordFragment()
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, changeFragment).commit()
        }

//        about fragment
        view.aboutcard.setOnClickListener {
            val aboutFragment = AboutFragment()
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, aboutFragment).commit()
        }

//        google Sync People Api
        view.synccard.setOnClickListener {
            if (GoogleSignIn.getLastSignedInAccount(requireActivity()) != null) {
                val googleAccount = GoogleSignIn.getLastSignedInAccount(requireActivity())
                if (!GoogleSignIn.hasPermissions(
                        googleAccount,
                        Scope(PeopleServiceScopes.CONTACTS_READONLY)
                    )
                ) {
                    GoogleSignIn.requestPermissions(
                        requireActivity(),
                        200,
                        googleAccount,
                        Scope(PeopleServiceScopes.CONTACTS_READONLY)
                    )
                } else {
                   googleSyncWorker()
                }
            }
        }
        return view
    }
    override fun onPause() {
        super.onPause()
        sharedPreferencesEditor.apply()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200){
            googleSyncWorker()
        }
    }
    private fun googleSyncWorker(){
        progressDialog.importdetails.text = "Contact Syncing..."
        progressDialog.setCancelable(false)
        progressDialog.show()
        val workManager = WorkManager.getInstance(requireContext())
        val googleContactSyncWorker = OneTimeWorkRequest.Builder(GoogleContactSyncWorker::class.java).build()
        workManager.enqueue(googleContactSyncWorker)

        workManager.getWorkInfoByIdLiveData(googleContactSyncWorker.id).observe(requireActivity()
            , androidx.lifecycle.Observer {
                println(it.state.name)
                when (it.state.name) {
                    "SUCCEEDED" -> {
                        progressDialog.dismiss()
//                         val contactListViewFragment = ContactListViewFragment()
//                         replaceFragment(contactListViewFragment)
                    }
                }
            })
    }

}