package com.example.contact.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.contact.R
import com.example.contact.activity.UserActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.people.v1.PeopleServiceScopes
import kotlinx.android.synthetic.main.progreesbar.*
import kotlinx.android.synthetic.main.settingfragment.view.*
import java.util.*


class SettingFragment :Fragment(R.layout.settingfragment) {
    val SCOPES = Arrays.asList(PeopleServiceScopes.CONTACTS_READONLY)
    val CREDENTIALS_FILE_PATH = "client_secret_653675639171-83k8kcdkaagbc4ukgfltquel4rk3kd3i.apps.googleusercontent.com.json"
    var HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    var json = GsonFactory.getDefaultInstance()
    lateinit var ele : Credential
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val user  = arguments?.get("email") as String?
        val view = inflater.inflate(R.layout.settingfragment,container,false)
        view.outcard.setOnClickListener {
            val googleSignin = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignin);
            if(GoogleSignIn.getLastSignedInAccount(requireActivity()) != null) {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("SignOut!!")
                    .setMessage("Sure You Want to sign out from this device?")
                    .setNeutralButton("No") { dialog, which ->
                    }
                    .setPositiveButton("Yes") { dialog, which ->
                            mGoogleSignInClient.signOut().addOnCompleteListener {
                            val progressDialog = Dialog(requireActivity())
                            progressDialog.setContentView(R.layout.progreesbar)
                            progressDialog.importdetails.setText("Signing Out...")
                            progressDialog.setCancelable(false)
                            progressDialog.show()
                            val intent =
                                Intent(requireActivity(), UserActivity::class.java)
                            Timer().schedule(object : TimerTask() {
                                override fun run() {
                                    progressDialog.dismiss()
                                    startActivity(intent)
                                    activity?.finish()
                                }
                            }, 3000)
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
                        val progressDialog = Dialog(requireActivity())
                        progressDialog.setContentView(R.layout.progreesbar)
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
        view.changepasscard.setOnClickListener{
            val changeFragment = ChangePasswordFragment()
            val bundle = Bundle()
            bundle.putString("email", user.toString())
            changeFragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, changeFragment).commit()
        }

        view.aboutcard.setOnClickListener {
            val aboutFragment = AboutFragment()
            val bundle = Bundle()
            bundle.putString("email", user.toString())
            aboutFragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.mainfragment, aboutFragment).commit()
        }

//      people
////        var parth =
//        var credential = GoogleAuthorizationCodeTokenRequest(
//            HTTP_TRANSPORT,
//            GsonFactory(),
//            "653675639171-158mhju5l891mg7711mdcbv0vtitqa17.apps.googleusercontent.com",
//            "{client_secret}",
//            "158mhju5l891mg7711mdcbv0vtitqa17",
//            "http://localhost"
//        ).execute()
//        println(credential)
//        println(requireActivity().assets.open("client_secret_653675639171-83k8kcdkaagbc4ukgfltquel4rk3kd3i.apps.googleusercontent.com.json"))
//        if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(requireContext(),"Permission Granted", Toast.LENGTH_SHORT).show()
////            GoogleContacts().execute()
//        }
//        else{
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),101)
//        }
//
////        println(json)
//        val clientSecrets = GoogleClientSecrets.load(json, InputStreamReader(inputdata))
//        println(clientSecrets)
//        println(HTTP_TRANSPORT)
////        var data =  JSONObject()
//            .put("token",)
//        val service =
//            PeopleService.Builder(HTTP_TRANSPORT,json, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName("Contact")
//                .build()
//        val clientSecrets = GoogleClientSecrets.load(json, InputStream(`in`))
        return view
    }
//    @Throws(IOException::class)
//    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
//        // Load client secrets.
//        val `in`: InputStream = requireActivity().assets.open(CREDENTIALS_FILE_PATH)
//        val clientSecrets = GoogleClientSecrets.load(json, InputStreamReader(`in`))
//        // Build flow and trigger user authorization request.
//        val fileDataStore = FileDataStoreFactory(File(requireContext().filesDir,"tokens"))
//        println(fileDataStore)
//        val flow = GoogleAuthorizationCodeFlow.Builder(
//            HTTP_TRANSPORT, json, clientSecrets, SCOPES)
//            .setDataStoreFactory(fileDataStore)
//            .setAccessType("offline")
//            .build()
//        println(flow)
//        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
//        println(receiver)
//        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
//    }
//    inner class GoogleContacts():AsyncTask<Void,Void,Void>(){
//        override fun doInBackground(vararg params: Void?): Void? {
//            ele = getCredentials(HTTP_TRANSPORT)
//            return null
//        }
//        override fun onPostExecute(result: Void?) {
//            println(ele)
//        }
//
//    }
}