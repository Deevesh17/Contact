package com.example.contact.model

import android.content.Context
import android.database.Cursor

class RecentSearchModel(val context: Context) {
    val logInDB = LogInDB(context)
    val searchList = ArrayList<String>()
    private fun getDataFromDB(email : String): Cursor?{
        try{
            return logInDB.getDetailedsearch(email)
        }catch (e : Exception){
            return null
        }
    }
    fun getSeachList(email : String) : ArrayList<String>{
        var cursor = getDataFromDB(email)
        if(cursor != null) {
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    searchList.add(cursor.getString(1))
                }
            }
        }
        return searchList.reversed() as ArrayList<String>
    }
}