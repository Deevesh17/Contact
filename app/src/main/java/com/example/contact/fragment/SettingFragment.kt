package com.example.contact.fragment

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.activity.UserActivity
import com.example.contact.viewmodel.ContactViewModel
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.progreesbar.*
import kotlinx.android.synthetic.main.settingfragment.view.*
import java.io.File
import java.util.*

class SettingFragment :Fragment(R.layout.settingfragment) {
    lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        val user  = arguments?.get("email") as String?
        val view = inflater.inflate(R.layout.settingfragment,container,false)
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progreesbar)


        val googleSignin = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN )
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignin);

//        signout Progress
        view.outcard.setOnClickListener {
            if(GoogleSignIn.getLastSignedInAccount(requireActivity()) != null) {
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
                            var tokensFilePath = File(requireActivity().filesDir,"tokens")
                            println(tokensFilePath.absolutePath)
                            println(tokensFilePath.canonicalFile.delete())
//                            var credentialsClient = CredentialsClient(requireActivity(),)
                            println(requireActivity().deleteDatabase("ContactDB"))

//                            val db = DBHelper(requireContext())
//                            db.dropDatabase()
//                           if(!tokensFilePath.exists()){

                               requireActivity().applicationContext.deleteFile(tokensFilePath.name)

                                val intent =
                                    Intent(requireActivity(), UserActivity::class.java)
                                Timer().schedule(object : TimerTask() {
                                    override fun run() {
                                        progressDialog.dismiss()
                                        startActivity(intent)
                                        activity?.finish()
                                    }
                                }, 3000)
//                            }
                        }
                    }
                    .show()
                }

            if (AccessToken.isCurrentAccessTokenActive()){
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
                                LoginManager.getInstance().logOut()
                                startActivity(intent)
                                activity?.finish()
                            }
                        }, 3000)
                    }
                    .show()
            }
        }

//        change password
        view.changepasscard.setOnClickListener{
            val changeFragment = ChangePasswordFragment()
            val bundle = Bundle()
            bundle.putString("email", user.toString())
            changeFragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, changeFragment).commit()
        }

//        about fragment
        view.aboutcard.setOnClickListener {
            val aboutFragment = AboutFragment()
            val bundle = Bundle()
            bundle.putString("email", user.toString())
            aboutFragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, aboutFragment).commit()
        }

//        google Sync People Api
        view.synccard.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                if(GoogleSignIn.getLastSignedInAccount(requireActivity()) != null){
                progressDialog.importdetails.text = "Contact Syncing..."
                progressDialog.setCancelable(false)
                progressDialog.show()
                val viewModel = ContactViewModel(requireContext())
                println(user)
                user?.let { it1 -> viewModel.setGoogleSync(it1,viewModel) }
                viewModel.SaveResult.observe(requireActivity(), androidx.lifecycle.Observer {
                    println(it)
                    progressDialog.dismiss()
                    val contactListViewFragment = ContactListViewFragment()
                    val bundle = Bundle()
                    bundle.putString("email", user.toString())
                    contactListViewFragment.arguments = bundle
                    parentFragmentManager.beginTransaction().replace(R.id.mainfragment, contactListViewFragment).commit()
                })
                }
            }
            else{
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),101)
            }
        }
        return view
    }
}