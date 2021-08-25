package com.example.contact.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginPasswordModelTest : TestCase(){
    @Test
    fun loginPasswordTest1(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val loginPasswordModel = LoginPasswordModel(appContext)
        Assert.assertEquals("Congratss!!,Success",
            loginPasswordModel.checkLoginPassword("deevesh@gmail.com", "09876543"))
    }
    @Test
    fun loginPasswordTest2(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val loginPasswordModel = LoginPasswordModel(appContext)
        Assert.assertEquals("Alert!!,Password Not Matched",
            loginPasswordModel.checkLoginPassword("hello@gmail.com", "09876543"))
    }
    @Test
    fun loginPasswordTest3(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val loginPasswordModel = LoginPasswordModel(appContext)
        Assert.assertEquals("Congratss!!,Success",
            loginPasswordModel.checkLoginPassword("hello@gmail.com", "12345678"))
    }
    @Test
    fun loginPasswordTest4(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val loginPasswordModel = LoginPasswordModel(appContext)
        Assert.assertEquals("Alert!!,User Not Exists",
            loginPasswordModel.checkLoginPassword("deeveshhello@gmail.com", "09876543"))
    }
    @Test
    fun loginPasswordTest5(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val loginPasswordModel = LoginPasswordModel(appContext)
        Assert.assertEquals("Alert!!,User Not Exists",
            loginPasswordModel.checkLoginPassword("", ""))
    }

}