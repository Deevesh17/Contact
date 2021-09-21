package com.example.contact.model

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.example.contact.musicroomdb.Audio
import com.example.contact.musicroomdb.AudioDB
import java.io.File

class AudioImportModel(val ctx : Context) {
    val audioDB = AudioDB.getDatabase(ctx)
    val audioDao = audioDB?.audioDao()
    fun addAudioFile() {
       val musicFile = Environment.getExternalStorageDirectory()
        fun importFile(file_ : File){
            val allFormatfile = file_.listFiles()
            for (mp3file in allFormatfile){
                if(mp3file.isDirectory){
                    importFile(mp3file)
                }
                else{
                   if(mp3file.name.endsWith(".mp3")){
                    audioDao?.insertFile(Audio(0,mp3file.name,mp3file))
                   }
                }
            }
        }
        importFile(musicFile)
//        val audio = ctx.contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            null,
//            null,
//            null,
//            null
//        )
//        if (audio != null) {
//            while (audio.moveToNext()) {
//                audioDao?.insertFile(Audio(0,audio.getString(audio.getColumnIndex(MediaStore.Audio.Media.TITLE))))
//            }
//            audio.close()
//        }
    }

}