package com.example.contact.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignupPasswordModelTest : TestCase()
{
    @Test
    fun signupPasswordTest1(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val signupPasswordModel = SignupPasswordModel(appContext)
        Assert.assertEquals("Alert!!,User Already Exists You can login by clicking Okay",
            signupPasswordModel.checkSignupPasswordAndWriteDB("Deevesh","deevesh@gmail.com", "7010071526","09876543","09876543"))
    }
    @Test
    fun signupPasswordTest2(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val signupPasswordModel = SignupPasswordModel(appContext)
        Assert.assertEquals("Congrats!!,You are Registered Successfully!!",
            signupPasswordModel.checkSignupPasswordAndWriteDB("Deevesh","hellodeevesh@gmail.com", "7010071526","09876543","09876543"))
    }
    @Test
    fun signupPasswordTest3(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val signupPasswordModel = SignupPasswordModel(appContext)
        Assert.assertEquals("Alert!!,Password and Confirm password is not same",
            signupPasswordModel.checkSignupPasswordAndWriteDB("Deevesh","newdeevesh@gmail.com", "7010071526","09876543","123456789"))
    }
    @Test
    fun signupPasswordTest4(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val signupPasswordModel = SignupPasswordModel(appContext)
        Assert.assertEquals("Alert!!,Password should contains more then 6 charecter",
            signupPasswordModel.checkSignupPasswordAndWriteDB("Deevesh","testdeevesh@gmail.com", "7010071526","076543","076543"))
    }

}