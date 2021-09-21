package com.example.contact.musicroomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(Audio::class),version = 2)
@TypeConverters(FileCoverter::class)
abstract class AudioDB : RoomDatabase() {
    abstract fun audioDao(): AudioDao
    companion object {
        val migrateDB = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Audio ADD COLUMN audioFilePath TEXT DEFAULT null")
            }
        }

        @Volatile
        private var INSTANCE: AudioDB? = null
        fun getDatabase(context: Context): AudioDB? {

            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AudioDB::class.java,
                    "audiodb"
                )
                    .addMigrations(migrateDB)
                    .build()
                INSTANCE = instance
            }
            return INSTANCE
        }
    }
}

