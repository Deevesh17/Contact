package com.example.contact.model

import android.content.Context
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CheckPasswordTest{
    lateinit var checkPassword: CheckPassword

    val mContextMock = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        checkPassword = CheckPassword(mContextMock)
    }

    @Test
    fun checkPasswordTest1(){
        Assert.assertEquals("Alert!!,Password should contains more then 6 charecter",checkPassword.checkPassword("12389","123456789"))
    }

    @Test
    fun checkPasswordTest2(){
        Assert.assertEquals("Alert!!,Password and Confirm password is not same",checkPassword.checkPassword("1234789","1756789"))
    }

    @Test
    fun checkPasswordTest3(){
        Assert.assertEquals("Success",checkPassword.checkPassword("123456789","123456789"))
    }
}