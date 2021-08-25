package com.example.contact.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckPasswordTest : TestCase(){
    @Test
    fun checkCreatePasswordTest1(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val checkPassword = CheckPassword(appContext)
        Assert.assertEquals("Alert!!,Password should contains more then 6 charecter",
            checkPassword.createNewPassword("kannan@gmail.com","12389", "123456789"))
    }
    @Test
    fun checkCreatePasswordTest2(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val checkPassword = CheckPassword(appContext)
        Assert.assertEquals("Alert!!,Password and Confirm password is not same",
            checkPassword.createNewPassword("kannan@gmail.com","1234567895", "123456789"))
    }
    @Test
    fun checkCreatePasswordTest3(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val checkPassword = CheckPassword(appContext)
        Assert.assertEquals("true",
            checkPassword.changeNewPassword("123456789","kannan@gmail.com", "123456789","123456789"))
    }
    @Test
    fun checkCreatePasswordTest4(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val checkPassword = CheckPassword(appContext)
        Assert.assertEquals("Alert!!,User Not Exists you can signup by clicking Okay",
            checkPassword.changeNewPassword("09876543","deevesh8hello@gmail.com", "7010071526","7010071526"))
    }
}