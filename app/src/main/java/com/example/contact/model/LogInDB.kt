package com.example.contact.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LogInDB(ctx: Context): SQLiteOpenHelper(ctx,"UserDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE UserDetails(name TEXT,email TEXT,password TEXT,number TEXT)")
        db?.execSQL("CREATE TABLE WeatherDetails(email TEXT,search TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop Table if exists UserDetails")
        db?.execSQL("drop Table if exists WeatherDetails")
    }
    fun insertWeatherDetails(email: String?,search: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("email", email)
        contentValues.put("search", search)
        val result = db.insert("WeatherDetails", null, contentValues)
        return result != -1L
    }
    fun deletesearch(email: String,search : String): Boolean{
        val dataBase = this.writableDatabase
        val cursor = dataBase.rawQuery("Select * from WeatherDetails where email = ? and search = ?", arrayOf(email,search))
        return if (cursor.count > 0) {
            val result = dataBase.delete("WeatherDetails", "email=? and search =?", arrayOf(email,search)).toLong()
            result != -1L
        } else false
    }
    fun updateWeatherSearch(email: String?,password: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("password", password)
        val cursor: Cursor =db.rawQuery("Select * from UserDetails where email = ?",arrayOf(email.toString()))
        return if(cursor.count > 0){
            val result = db.update("WeatherDetails",contentValues,"email=?",arrayOf(email.toString()))
            result != -1
        } else false
    }
    fun insertuserdata(name: String?,email: String?,password: String?,number: String?): Boolean? {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("email", email)
        contentValues.put("password", password)
        contentValues.put("number", number)
        val result = db.insert("UserDetails", null, contentValues)
        return result != -1L
    }
    fun updateuserdata(email: String?,password: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("password", password)
        val cursor: Cursor =db.rawQuery("Select * from UserDetails where email = ?",arrayOf(email.toString()))
        return if(cursor.count > 0){
            val result = db.update("UserDetails",contentValues,"email=?",arrayOf(email.toString()))
            result != -1
        } else false
    }
    fun getDetailedData(email : String): Cursor? {
        val database = this.writableDatabase
        return database.rawQuery("Select * from UserDetails where email = ?", arrayOf(email))
    }
    fun getDetailedsearch(email : String): Cursor? {
        val database = this.writableDatabase
        return database.rawQuery("Select * from WeatherDetails where email = ? ", arrayOf(email))
    }
}