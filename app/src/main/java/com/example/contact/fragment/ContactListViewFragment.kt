package com.example.contact.fragment

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contactlistviewfragment.view.*
import kotlinx.android.synthetic.main.navheader.view.*
import kotlinx.android.synthetic.main.progreesbar.*

class ContactListViewFragment : Fragment(R.layout.contactlistviewfragment) {
    var contactList :ArrayList<ContactData> = ArrayList()
    lateinit var contactDb : DBHelper
    lateinit var listView : RecyclerView
    lateinit var title : ContactViewModel
    var selectedList :ArrayList<ContactData> = ArrayList()
    var signinUser : String? = null
    lateinit var sharedPreferences: SharedPreferences
    var contactAdapter : ContactAdapter = ContactAdapter()
    lateinit var progressDialog : Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        title = ContactViewModel((requireContext()))
        requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = true
        requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.exportfile).isVisible = true
        requireActivity().topAppBarmain.menu.findItem(R.id.recent).isVisible = false
        requireActivity().topAppBarmain.title = "Contact"
        requireActivity().topAppBarmain.subtitle = ""

        requireActivity().topAppBarmain.setNavigationIcon(R.drawable.ic_action_menu)

//        title.setAudioCount(title)
//        title.audioCountResult.observe(requireActivity(), Observer {
//            if(it != null){
//                headerView.audiocount.text = "Audio List Available is $it"
//            }
//        })

        sharedPreferences = requireActivity().getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)

        val view = inflater.inflate(R.layout.contactlistviewfragment,container,false)
        listView = view.listview
        createAdapter()

        view.navigationhome.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.contact -> {
                    val listfrag = ContactListViewFragment()
                    parentFragmentManager.beginTransaction().replace(R.id.mainfragment, listfrag).commit()
                }
                R.id.weather ->{
                    val weather = WeatherFragment()
                    parentFragmentManager.beginTransaction().replace(R.id.mainfragment, weather).commit()
                }
            }
            true
        }
        contactDb = DBHelper(requireContext())


//        progress bar initialization
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.progreesbar)

        signinUser = sharedPreferences.getString("email","")

//        navigation button handle in Tolbar
        requireActivity().topAppBarmain.setNavigationOnClickListener {
            requireActivity().maindrawable.openDrawer(GravityCompat.START)
        }

//        Menu details available in toolbar
        requireActivity().topAppBarmain.setOnMenuItemClickListener {
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
                requireActivity().topAppBarmain.title = it
                requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = true
                requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
                requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = true
                requireActivity().topAppBarmain.menu.findItem(R.id.exportfile).isVisible = true

            }else{
                requireActivity().topAppBarmain.title = "Contact"
                requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
                requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = true
                requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
            }
        })
        return view
    }
    override fun onResume() {
        super.onResume()
        contactList.clear()
        val headerView = requireActivity().sidenav.getHeaderView(0)
        signinUser?.let { title.setDbData("GetDB",selectedList, it,title) }
        title.contactList.observe(requireActivity(), Observer {
            if(it != null){
                progressDialog.dismiss()
                contactList = it
                headerView.contactcount.text = "Contact Available is ${contactList.size}"
//                createAdapter(it)
                signinUser?.let { it1 -> contactAdapter.setData(it,title, it1) }
            }
        })
    }

//  Event Handling for different menu in menu bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.importfile -> {
                try{
                    if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
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
                        signinUser?.let { title.setDbData("Delete",selectedList, it,title) }

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

    fun createAdapter()  {
//        contactAdapter = signinUser?.let { ContactAdapter() }!!
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = layoutManager
        listView.adapter = contactAdapter
    }


}