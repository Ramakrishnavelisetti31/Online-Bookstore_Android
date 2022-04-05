package com.example.bookstore.utils

import org.junit.Assert.*
import org.junit.Test

class UtilsTest {

    private val utils = Utils()
    @Test
    fun givenEmailId_IsProper_ReturnTrue() {
        val actualResult: Boolean =
            utils.emailId("ramakrishna96.velisetti@gmail.com")
        assertEquals(true, actualResult)
    }

    @Test
    fun givenEmailId_IsNotProper_ReturnFalse() {
        val actualResult: Boolean =
            utils.emailId("ramakrishna96.velisettigmail.com")
        assertEquals(false, actualResult)
    }

    @Test
    fun givenPasswordRule1_IsProper_ReturnTrue() {
        val actualResult: Boolean = utils.passwordRule1("qwgthjkdl")
        assertEquals(true, actualResult)
    }

    @Test
    fun givenPasswordRule1_IsNotProper_ReturnFalse() {
        val actualResult: Boolean = utils.passwordRule1("jnsdsjRcvc")
        assertEquals(false, actualResult)
    }

    @Test
    fun givenPasswordRule3_IsProper_ReturnTrue() {
        val actualResult: Boolean = utils.passwordRule2("qw98hRLkdl")
        assertEquals(true, actualResult)
    }


}