package com.example.contact.model

import android.content.Context
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SignupPasswordModelTest{

    lateinit var signupPasswordModel: SignupPasswordModel

    val mContextMock = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        signupPasswordModel = SignupPasswordModel(mContextMock)
    }

    @Test
    fun checkPasswordTest1(){
        Assert.assertEquals("Alert!!,Password should contains more then 6 charecter",signupPasswordModel.checkPassword("12389","123456789"))
    }

    @Test
    fun checkPasswordTest2(){
        Assert.assertEquals("Alert!!,Password and Confirm password is not same",signupPasswordModel.checkPassword("1234789","1756789"))
    }

    @Test
    fun checkPasswordTest3(){
        Assert.assertEquals("Success",signupPasswordModel.checkPassword("123456789","123456789"))
    }
}