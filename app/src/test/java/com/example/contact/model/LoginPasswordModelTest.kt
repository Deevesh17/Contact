package com.example.contact.model

import android.content.Context
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LoginPasswordModelTest {

    lateinit var loginPasswordModel: LoginPasswordModel

    val mContextMock = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
       loginPasswordModel = LoginPasswordModel(mContextMock)
    }

    @Test
    fun checkPasswordTest1(){
        Assert.assertEquals("Congratss!!,Success",loginPasswordModel.checkPassword("123456789","123456789"))
    }

    @Test
    fun checkPasswordTest2(){
        Assert.assertEquals("Alert!!,Password Not Matched",loginPasswordModel.checkPassword("123456789","1256789"))
    }
}