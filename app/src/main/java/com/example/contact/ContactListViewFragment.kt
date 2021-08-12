package com.example.contact

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.contact.model.ContactData
import com.example.contact.model.DBHelper
import com.example.contact.viewmodel.ContactViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.contactlistviewfragment.view.*
import kotlinx.android.synthetic.main.progreesbar.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ContactListViewFragment : Fragment(R.layout.contactlistviewfragment) {
    var contactList :ArrayList<ContactData> = ArrayList()
    lateinit var contactDb : DBHelper
    lateinit var listView : RecyclerView
    lateinit var title : ContactViewModel
    var selectedList :ArrayList<ContactData> = ArrayList()
    var signinUser = ""
    private lateinit var contactAdapter :ContactAdapter
    lateinit var progressDialog : Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.contactlistviewfragment,container,false)
        contactDb = DBHelper(requireContext())
        title = ContactViewModel((requireContext()))
        signinUser = arguments?.getString("email").toString()
        progressDialog = Dialog(requireContext())
        listView = view.listview
        progressDialog.setContentView(R.layout.progreesbar)
        view.topAppBarmain.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        view.topAppBarmain.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        view.create.setOnClickListener{
            val intent = Intent(requireContext(),CreateContactActivity::class.java)
            intent.putExtra("email",signinUser)
            startActivity(intent)
        }
        if(context?.let {
                ActivityCompat.checkSelfPermission(it,
                    Manifest.permission.READ_CONTACTS)
            } != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CONTACTS),55)
        }

        view.searchfromlist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contactAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        title.contactDataLive.observe(requireActivity(), Observer {
            if (contactAdapter.selectedCount > 0){
                view.topAppBarmain.title = it
                view.topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = true
                view.topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
                view.topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = true
                view.topAppBarmain.menu.findItem(R.id.exportfile).isVisible = true

            }else{
                view.topAppBarmain.title = "Contact"
                view.topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
                view.topAppBarmain.menu.findItem(R.id.importfile).isVisible = true
                view.topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
            }
        })
        return view
    }
    override fun onResume() {
        super.onResume()
        contactList.clear()
        ContactTask().execute("GetDB")
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.importfile -> {
                try{
                    if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
//                        createWorker()
                        val intent = Intent(activity,SelectContactActivity::class.java)
                        intent.putExtra("email",signinUser)
                        startActivity(intent)
                    }else{
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CONTACTS),56)
                    }
                }catch (e : Exception){
                    println(e)
                }
            }
            R.id.exportfile ->{
                try{
                    if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        writeContact()
                    }else{
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE),25)
                    }
                }catch (e : Exception){
                    println(e)
                }
            }
            R.id.Deletefilemain ->{

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Contacts")
                    .setMessage("Delete the Selected contacts info from this device?")
                    .setNeutralButton("Cancel") { dialog, which ->
                    }
                    .setPositiveButton("Delete") { dialog, which ->
                        progressDialog.importdetails.setText("Deleting...")
                        progressDialog.setCancelable(false)
                        progressDialog.show()
                        if(contactAdapter.isSelectAll){
                            selectedList = ArrayList<ContactData>(contactList)
                        }else{
                            selectedList = contactAdapter.getSelectedContactList()
                        }
                        if(contactAdapter.selectedCount > 0){
                            contactAdapter.selectedPosition.clear()
                            contactAdapter.selectedCount = 0
                            contactAdapter.removedPosition.clear()
                            title.setValutodata("Selected(${contactAdapter.selectedCount}/${contactList.size})")
                        }
                        ContactTask().execute("Delete")
                        contactList.clear()
                        ContactTask().execute("GetDB")

                    }
                    .show()
            }
            R.id.selectAllmain ->{
                contactAdapter.isSelectAll =  !contactAdapter.isSelectAll
                if(!contactAdapter.isSelectAll){
                    contactAdapter.selectedPosition.clear()
                    contactAdapter.selectedCount = 0
                    contactAdapter.removedPosition.clear()
                    title.setValutodata("Selected(${contactAdapter.selectedCount}/${contactList.size})")
                }else  {
                    contactAdapter.selectedCount = contactList.size
                    title.setValutodata("Selected(${contactAdapter.selectedCount}/${contactList.size})")
                }
                contactAdapter.notifyDataSetChanged()
            }
            R.id.signout ->{
                val googleSignin = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN  )
                    .requestEmail()
                    .build()
                val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignin);
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    progressDialog.importdetails.setText("Signing Out...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    var intent = Intent(requireContext(),UserActivity::class.java)
                    Timer().schedule(object : TimerTask(){
                        override fun run() {
                            progressDialog.dismiss()
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    },3000)

                }
            }

        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 56){
            if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(requireContext(),SelectContactActivity::class.java)
                startActivity(intent)
            }
        }
        if(requestCode == 25){
            if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                writeContact()
            }
        }
        if(requestCode == 55){
            if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(requireContext(),"Permission Denied", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(),"Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onPause() {
        super.onPause()
        progressDialog.dismiss()
    }
    private fun writeContact(){
        Toast.makeText(requireContext(),"Export", Toast.LENGTH_SHORT).show()
        var data = Data.Builder()
            .putString("email",signinUser)
            .build()
        val workManager = WorkManager.getInstance(requireContext())
        val export = OneTimeWorkRequest.Builder(ExportContact::class.java).setInputData(data).build()
        workManager.enqueue(export)
    }
    //     fun createWorker(){
//         dialog.importdetails.setText("Importing...")
//         dialog.setCancelable(false)
//         dialog.show()
//         var workManager = WorkManager.getInstance(applicationContext)
//         val import = OneTimeWorkRequest.Builder(ImportWorker::class.java).build()
//         workManager.enqueue(import)
//         workManager.getWorkInfoByIdLiveData(import.id).observe(this, Observer {
//             if(it.state.name == "SUCCEEDED"){
//                 contactList.clear()
//                 ContactTask().execute()
//             }
//         })
//     }
    fun createAdapter(contact : ArrayList<ContactData>)  {
        contactAdapter = ContactAdapter(contact, title,signinUser)
        listView.adapter = contactAdapter
    }
    inner class ContactTask : AsyncTask<String, Int, Void>() {
        private var cursorTotal = 0
        var params = ""
        override fun doInBackground(vararg params: String?): Void? {
            this.params = params[0].toString()
            if (params[0] == "GetDB") {
                val cursor: Cursor? = contactDb.getdata(signinUser)
                var count = 0
                cursorTotal = cursor?.count!!
                try {
                    if (cursorTotal != 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getString(3) != null) {
                                val compressed = Base64.decode(cursor.getString(3), Base64.DEFAULT)
                                contactList.add(
                                    ContactData(
                                        cursor.getInt(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        BitmapFactory.decodeByteArray(compressed,
                                            0,
                                            compressed.size),
                                        cursor.getString(4),
                                        cursor.getString(5),
                                        cursor.getString(6),
                                        cursor.getString(7),
                                        cursor.getString(8),
                                        cursor.getString(9),
                                        cursor.getString(10)
                                    )
                                )
                            } else {
                                contactList.add(ContactData(
                                    cursor.getInt(0),
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    null,
                                    cursor.getString(4),
                                    cursor.getString(5),
                                    cursor.getString(6),
                                    cursor.getString(7),
                                    cursor.getString(8),
                                    cursor.getString(9),
                                    cursor.getString(10)
                                ))
                            }
                            publishProgress(count)
                            count++

                        }
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }
            else if (params[0] == "Delete"){
                if(selectedList.size > 0){
                    for(contactData in selectedList){
                        contactDb.deletedata(contactData.contactId)
                    }
                }
            }
            return null
        }
        override fun onProgressUpdate(vararg values: Int?) {
            if(contactList.size % 10 == 0){
                createAdapter(contactList)
            }
        }
        override fun onPostExecute(result: Void?) {
            if (params == "GetDB") {
                createAdapter(contactList)
            }
            progressDialog.dismiss()
        }
    }
}