package com.example.contact.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contact.model.ImportData

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


//     fun createWorker(){
//         dialog.importdetails.setText("Importing...")
//         dialog.setCancelable(false)
//         dialog.show()
//         var workManager = WorkManager.getInstance(applicationContext)
//         val import = OneTimeWorkRequest.Builder(ImportWorker::class.java).build()
//         workManager.enqueue(import)
//         workManager.getWorkInfoByIdLiveData(import.id).observe(this, Observer {
//             if(it.state.name == "SUCCEEDED"){
//                 contactList.clear()
//                 ContactTask().execute()
//             }
//         })
//     }