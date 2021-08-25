package com.example.contact.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForgetPasswordModelTest : TestCase(){
    @Test
    fun forgetNumbercheckTest1(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val forgetPasswordModel = ForgetPasswordModel(appContext)
        Assert.assertEquals("Success",
            forgetPasswordModel.checkForgetPasswordUser("deevesh@gmail.com", "7010071526"))
    }
    @Test
    fun forgetNumbercheckTest2(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val forgetPasswordModel = ForgetPasswordModel(appContext)
        Assert.assertEquals("Alert!!,Email and Mobile Number are not same",
            forgetPasswordModel.checkForgetPasswordUser("deevesh@gmail.com", "9524214498"))
    }
    @Test
    fun forgetNumbercheckTest3(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val forgetPasswordModel = ForgetPasswordModel(appContext)
        Assert.assertEquals("Alert!!,User Not Exists you can signup by clicking Okay",
            forgetPasswordModel.checkForgetPasswordUser("deeveshhello@gmail.com", "7010071526"))
    }
}