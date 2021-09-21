package com.example.contact.musicroomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AudioDao {
    @Insert
    fun insertFile(vararg audio: Audio)

    @Query("SELECT * FROM Audio ORDER BY audioTitle ASC")
    fun getAllAudioFile(): List<Audio>

    @Query("SELECT Count(*) FROM Audio")
    fun getFileCount(): Int

    @Query("DELETE FROM Audio")
    fun DeleteFile()

}