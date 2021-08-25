package com.example.contact

import android.content.Context
import com.example.contact.model.LoginPasswordModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginPasswordTest  {
    @Mock
    private lateinit var mockContext: Context
    @Test
    fun loginPasswordTest1(){
        `when`(mockContext.getString(R.string.app_name)).thenReturn("Contact")
        val loginPasswordModel = LoginPasswordModel(mockContext)
        assertEquals("Congratss!!,Success",loginPasswordModel.checkLoginPassword("deevesh@gmail.com","09874563"))
    }
    @Test
    fun loginPasswordTest2(){
        `when`(mockContext.getString(R.string.app_name)).thenReturn("Contact")
        val loginPasswordModel = LoginPasswordModel(mockContext)
        assertEquals("Alert!!,User Not Exsts",loginPasswordModel.checkLoginPassword("deevesh@gmail.com","09874563"))
    }
    @Test
    fun loginPasswordTest3(){
        `when`(mockContext.getString(R.string.app_name)).thenReturn("Contact")
        val loginPasswordModel = LoginPasswordModel(mockContext)
        assertEquals("0",loginPasswordModel.checkLoginPassword("deevesh@gmail.com","09874563"))
    }
    @Test
    fun loginPasswordTest4(){
        `when`(mockContext.getString(R.string.app_name)).thenReturn("Contact")
        val loginPasswordModel = LoginPasswordModel(mockContext)
        assertEquals("Alert!!,User Not Exists",loginPasswordModel.checkLoginPassword("deevesh@gmail.com","09874563"))
    }
    @Test
    fun loginPasswordTest5(){
        `when`(mockContext.getString(R.string.app_name)).thenReturn("Contact")
        val loginPasswordModel = LoginPasswordModel(mockContext)
        assertEquals("Alert!!,User Not Exists",loginPasswordModel.checkLoginPassword("deevesh@gmail.com","09874563"))
    }
}