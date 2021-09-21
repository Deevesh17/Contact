package com.example.contact.musicroomdb

import androidx.room.TypeConverter
import java.io.File

class FileCoverter {
    @TypeConverter
    fun fromFile(value : File?) : String?{
        return value?.toString()
    }
    @TypeConverter
    fun toFile(value : String?): File?{
        return value?.let { File(it) }
    }
}