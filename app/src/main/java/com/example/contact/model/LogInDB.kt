package com.example.contact.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contact.SignupActivity

class LogInDB(val ctx: Context): SQLiteOpenHelper(ctx,"UserDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE UserDetails(name TEXT,email TEXT,password TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop Table if exists UserDetails")
    }
    fun insertuserdata(name: String?,email: String?,password: String?): Boolean? {
        println("$name $password")
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("email", email)
        contentValues.put("password", password)
        val result = db.insert("UserDetails", null, contentValues)
        println(result)
        return result != -1L
    }
    fun getDetailedData(username : String): Cursor? {
        println(username)
        val database = this.writableDatabase
        return database.rawQuery("Select * from UserDetails where name = ?", arrayOf(username))
    }
}