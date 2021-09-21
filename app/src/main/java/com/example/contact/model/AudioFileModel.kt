package com.example.contact.model

import android.content.Context
import com.example.contact.musicroomdb.Audio
import com.example.contact.musicroomdb.AudioDB
import com.example.contact.viewmodel.ContactViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioFileModel(val ctx : Context, val viewModel: ContactViewModel) {

    val audioDB = AudioDB.getDatabase(ctx)
    val audioDao = audioDB?.audioDao()
    fun addAudioFile(){
      CoroutineScope(Dispatchers.IO).launch {
            insertAudioFile()
       }
    }
    suspend fun insertAudioFile(){
        val audioImportModel = AudioImportModel(ctx)
        audioImportModel.addAudioFile()
        audiofilefromDB()
    }
     fun getCountOfFileCoroutine(){
        CoroutineScope(Dispatchers.IO).launch {
         getCountFile()
        }

    }
    suspend fun getCountFile(){
        val count = audioDao?.getFileCount()
        audioCount(count.toString())
    }
    private fun setAudioCount(count : String){
        viewModel.audioCountResult.value = count
    }
    private suspend fun audioCount(audioCount : String){
        withContext(Dispatchers.Main){
            setAudioCount(audioCount)
        }
    }
    fun getAudioFile(){
        CoroutineScope(Dispatchers.IO).launch {
            audiofilefromDB()
        }
    }
    suspend fun audiofilefromDB(){
        val audioFile = audioDao?.getAllAudioFile()
        if (audioFile != null) {
            audioFileData(audioFile)
        }
    }
    private fun setAudiofile(audio : List<Audio>){
        viewModel.audiofile.value = audio
    }
    private suspend fun audioFileData(audio : List<Audio>){
        withContext(Dispatchers.Main){
            setAudiofile(audio)
        }
    }
}