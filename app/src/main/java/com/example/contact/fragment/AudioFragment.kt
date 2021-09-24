package com.example.contact.fragment

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contact.R
import com.example.contact.adapter.AudioAdapter
import com.example.contact.model.AudioFileModel
import com.example.contact.model.RemoteConfigUtills
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_audio.view.*
import kotlinx.android.synthetic.main.progreesbar.*

class AudioFragment : Fragment(R.layout.fragment_audio) {
    lateinit var viewModel : ContactViewModel
    lateinit var progressDialog: Dialog
    lateinit var fragview : View
    private var audioAdapter = AudioAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_audio,container,false)
        requireActivity().topAppBarmain.menu.findItem(R.id.Deletefilemain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.importfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.selectAllmain).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.exportfile).isVisible = false
        requireActivity().topAppBarmain.menu.findItem(R.id.recent).isVisible = false
        requireActivity().topAppBarmain.title = "Audio"
        requireActivity().topAppBarmain.setBackgroundColor(Color.parseColor(RemoteConfigUtills.getAudioToolBarBackground()))
        requireActivity().topAppBarmain.subtitle = "Welcome ${arguments?.getString("userName")}"
        requireActivity().topAppBarmain.setNavigationIcon(R.drawable.ic_action_menu)
        requireActivity().topAppBarmain.setNavigationOnClickListener {
            requireActivity().maindrawable.openDrawer(GravityCompat.START)
        }
        viewModel = ContactViewModel(requireContext())
        viewModel.setAudioFile(viewModel)
        progressDialog = Dialog(requireContext())


//        val args : AudioFragmentArgs by navArgs()
//        println(args.sinedUser)
//
//        val signedUser = AudioFragmentArgs.fromBundle(requireArguments())
//        println(signedUser.sinedUser)
        view.audiofile.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                progressDialog.setContentView(R.layout.progreesbar)
                progressDialog.importdetails.setText("Uploading Audio Files")
                progressDialog.setCancelable(false)
                progressDialog.show()
                val audioFilModel = AudioFileModel(requireContext(),viewModel)
                audioFilModel.addAudioFile()

            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    56)
            }
        }
        fragview= view
        createAdapter()
        return view
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent("update.audio.count")
        viewModel.audiofile.observe(requireActivity(), Observer {
            audioAdapter.setData(it,requireContext())
            progressDialog.dismiss()
            intent.putExtra("audioCount",it.size)
            requireActivity().sendBroadcast(intent)
        })
    }

    override fun onPause() {
        super.onPause()
        if(audioAdapter.play != null)
        {
            if(audioAdapter.play?.isPlaying!!){
                audioAdapter.play?.stop()
                audioAdapter.play?.release()
                audioAdapter.play = null
                audioAdapter.lastPlayedImage.let { it?.setImageResource(R.drawable.play) }
            }
        }
    }

    fun createAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        fragview.audiolist.layoutManager = layoutManager
        fragview.audiolist.adapter = audioAdapter
    }

}