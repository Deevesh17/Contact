package com.example.contact

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.util.Output
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contact.model.ContactData
import com.example.contact.model.ImportData
import com.example.contact.viewmodel.ContactViewModel

class ImportWorker(var context: Context, var workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
       try {
           ImportData(applicationContext).addContactData()
           return Result.success()
       }catch (e:Exception){
           return Result.failure()
       }
    }
}