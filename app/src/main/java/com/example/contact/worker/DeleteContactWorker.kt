package com.example.contact.worker

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contact.model.DBHelper
import com.example.contact.musicroomdb.AudioDB

class DeleteContactWorker(val context: Context,workerParameters: WorkerParameters) : Worker(context,workerParameters) {
    private val contactDb = DBHelper(context)
    lateinit var sharedPreferences: SharedPreferences
    var cursorTotal : Int = 0
    val audioDB = AudioDB.getDatabase(context)
    val audioDao = audioDB?.audioDao()

    override fun doWork(): Result {
        when {
            deleteContactFromDB() == 0 -> return Result.success()
            else -> return Result.failure()
        }
    }
    fun deleteContactFromDB() : Int{
        sharedPreferences = context.getSharedPreferences("com.example.contact.user",
            Context.MODE_PRIVATE)
        val signinUser  = sharedPreferences.getString("email","")
        var cursor: Cursor?
        try {
            cursor = contactDb.getdata(signinUser)
            cursorTotal = cursor?.count!!
            println(cursorTotal)
            if (cursorTotal != 0 && cursor != null) {
                while (cursor.moveToNext()) {
                    contactDb.deletedata(cursor.getInt(0))
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        cursor = contactDb.getdata(signinUser)

        audioDao?.DeleteFile()
        return if(cursor?.count == 0)
            0
        else 1
    }
}