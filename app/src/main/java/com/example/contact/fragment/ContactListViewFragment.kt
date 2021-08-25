package com.example.contact.fragment

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.contact.R
import com.example.contact.activity.CreateContactActivity
import com.example.contact.activity.SelectContactActivity
import com.example.contact.adapter.ContactAdapter
import com.example.contact.model.ContactData
import com.example.contact.model.DBHelper
import com.example.contact.viewmodel.ContactViewModel
import com.example.contact.worker.ExportContact
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.contactlistviewfragment.view.*
import kotlinx.android.synthetic.main.progreesbar.*

class ContactListViewFragment : Fragment(R.layout.contactlistviewfragment) {
    var contactList :ArrayList<ContactData> = ArrayList()
    lateinit var contactDb : DBHelper
    lateinit var listView : RecyclerView
    lateinit var title : ContactViewModel
    var selectedList :ArrayList<ContactData> = ArrayList()
    var signinUser = ""
    private lateinit var contactAdapter : ContactAdapter
    lateinit var progressDialog : Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.contactlistviewfragment,container,false)
        listView = view.listview

        contactDb = DBHelper(requireContext())
        title = ContactViewModel((requireContext()))

//        progress bar initialization
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progreesbar)

        signinUser = arguments?.getString("email").toString()

//        navigation button handle in Tolbar
        view.topAppBarmain.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

//        Menu details available in toolbar
        view.topAppBarmain.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

//        creating contact floating bar
        view.create.setOnClickListener{
            val intent = Intent(requireContext(), CreateContactActivity::class.java)
            intent.putExtra("email",signinUser)
            startActivity(intent)
        }

//        checking permission to read contact from Mobile phone book
        if(requireContext().let {
                ActivityCompat.checkSelfPermission(it,
                    Manifest.permission.READ_CONTACTS)
            } != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CONTACTS),55)
        }

//        searchbar
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
        title.setDbData("GetDB",selectedList,signinUser,title)
        title.contactList.observe(requireActivity(), Observer {
            if (it != null){
                progressDialog.dismiss()
                contactList = it
                println(it)
                createAdapter(it)
            }
        })
    }

//  Event Handling for different menu in menu bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.importfile -> {
                try{
                    if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
//                        createWorker()
                        val intent = Intent(activity, SelectContactActivity::class.java)
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
                        if(contactAdapter.isSelectAll) selectedList = ArrayList(contactList) else selectedList = contactAdapter.getSelectedContactList()
                        if(contactAdapter.selectedCount > 0){
                            contactAdapter.selectedPosition.clear()
                            contactAdapter.selectedCount = 0
                            contactAdapter.removedPosition.clear()
                            title.setValutodata("Selected(${contactAdapter.selectedCount}/${contactList.size})")
                        }
                        title.setDbData("Delete",selectedList,signinUser,title)
                        title.setDbData("GetDB",selectedList,signinUser,title)

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
        }
        return true
    }

//    event done after permission requested
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 56){
            if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(requireContext(), SelectContactActivity::class.java)
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
        val data = Data.Builder()
            .putString("email",signinUser)
            .build()
        val workManager = WorkManager.getInstance(requireContext())
        val export = OneTimeWorkRequest.Builder(ExportContact::class.java).setInputData(data).build()
        workManager.enqueue(export)
    }

    fun createAdapter(contact : ArrayList<ContactData>)  {
        contactAdapter = ContactAdapter(contact, title,signinUser)
        listView.adapter = contactAdapter
    }


}