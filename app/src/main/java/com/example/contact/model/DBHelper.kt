package com.example.contact.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(val ctx : Context) : SQLiteOpenHelper(ctx,"ContactDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ContactDetails(id  INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,number TEXT,image TEXT,company TEXT,email TEXT,contactgroup TEXT,address TEXT,nickname TEXT,website TEXT,notes TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop Table if exists ContactDetails")
    }
    fun insertuserdata(name: String?, number: String?, image: String?,company: String?,email: String?,contactgroup: String?,
                       address: String?,nickname: String?,website: String?,notes: String?): Boolean? {
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
        val result = db.insert("ContactDetails", null, contentValues)
        if (result == -1L) return false
        else return true
    }
    fun deletedata(contactId: Int): Boolean? {
        val DB = this.writableDatabase
        val cursor = DB.rawQuery("Select * from ContactDetails where id = $contactId", null)
        if (cursor.count > 0) {
            val result = DB.delete("ContactDetails", "id=?", arrayOf(contactId.toString())).toLong()
            if (result == -1L) return false
            else  return true
        } else return false
    }
    fun updateuserdata(name: String?, number: String?,company: String?,email: String?,contactgroup: String?,
                       address: String?,nickname: String?,website: String?,notes: String?, id : Int,image : String?): Boolean? {
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
        if(cursor.count > 0){
            var result = db.update("ContactDetails",contentValues,"id=?",arrayOf(id.toString()))
            if (result == -1) return false
            else return true
        }
        else return false
    }
    fun getdata(): Cursor? {
        val DB = this.writableDatabase
        return DB.rawQuery("Select * from ContactDetails as detail order by detail.name Asc ", null)
    }
    fun getDetailedData(contactId : Int): Cursor? {
        val DB = this.writableDatabase
        return DB.rawQuery("Select * from ContactDetails as detail where detail.id = $contactId", null)
    }
}