package com.example.contact.model

import android.content.Context
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ForgetPasswordModelTest {

    lateinit var forgetPasswordModel: ForgetPasswordModel

    val mContextMock = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        forgetPasswordModel = ForgetPasswordModel(mContextMock)
    }

    @Test
    fun CheckMobileTest1(){
        Assert.assertEquals("Success",forgetPasswordModel.checkMobileNumber("deevesh@gmail.com","deevesh@gmail.com","123456789","123456789"))
    }

    @Test
    fun CheckMobileTest2(){
        Assert.assertEquals("Alert!!,Email and Mobile Number are not same",forgetPasswordModel.checkMobileNumber("deeveshdeevesh@gmail.com","deevesh@gmail.com","1234568","123456789"))
    }

}