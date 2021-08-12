package com.example.contact.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(ctx : Context) : SQLiteOpenHelper(ctx,"ContactDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ContactDetails(id  INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,number TEXT,image TEXT,company TEXT,email TEXT,contactgroup TEXT,address TEXT,nickname TEXT,website TEXT,notes TEXT,user Text)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop Table if exists ContactDetails")
    }
    fun insertuserdata(name: String?, number: String?, image: String?,company: String?,email: String?,contactgroup: String?,
                       address: String?,nickname: String?,website: String?,notes: String?,user: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("number", number)
        contentValues.put("image", image)
        contentValues.put("company", company)
        contentValues.put("email", email)
        contentValues.put("contactgroup", contactgroup)
        contentValues.put("address", address)
        contentValues.put("nickname", nickname)
        contentValues.put("website", website)
        contentValues.put("notes", notes)
        contentValues.put("user", user)
        val result = db.insert("ContactDetails", null, contentValues)
        return result != -1L
    }
    fun deletedata(contactId: Int): Boolean{
        val dataBase = this.writableDatabase
        val cursor = dataBase.rawQuery("Select * from ContactDetails where id = $contactId", null)
        return if (cursor.count > 0) {
            val result = dataBase.delete("ContactDetails", "id=?", arrayOf(contactId.toString())).toLong()
            result != -1L
        } else false
    }
    fun updateuserdata(name: String?, number: String?,company: String?,email: String?,contactgroup: String?,
                       address: String?,nickname: String?,website: String?,notes: String?, id : Int,image : String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("number", number)
        contentValues.put("image", image)
        contentValues.put("company", company)
        contentValues.put("email", email)
        contentValues.put("contactgroup", contactgroup)
        contentValues.put("address", address)
        contentValues.put("nickname", nickname)
        contentValues.put("website", website)
        contentValues.put("notes", notes)
        val cursor: Cursor =db.rawQuery("Select * from ContactDetails where id = $id",null)
        return if(cursor.count > 0){
            val result = db.update("ContactDetails",contentValues,"id=?",arrayOf(id.toString()))
            result != -1
        } else false
    }
    fun getdata(username: String?): Cursor? {
        val database = this.writableDatabase
        return database.rawQuery("Select * from ContactDetails  where user = ? order by name Asc ", arrayOf(username))
    }
    fun getDetailedData(contactId : Int): Cursor? {
        val database = this.writableDatabase
        return database.rawQuery("Select * from ContactDetails as detail where detail.id = $contactId", null)
    }
}