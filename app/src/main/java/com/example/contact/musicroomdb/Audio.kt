package com.example.contact.musicroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity(tableName = "audio")
data class Audio(
    @PrimaryKey(autoGenerate = true) var audioId: Int,
    var audioTitle: String,
    var audioFilePath: File?
)