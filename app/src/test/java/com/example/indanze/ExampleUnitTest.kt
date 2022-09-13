package com.example.indanze

import android.text.TextUtils
import org.junit.Test
import org.junit.Assert.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun testWithEmptyEmail(){

        var email  = "" // empty email
        var password = "123123"
        var username = "Leox"

        val operationSuccess : Boolean = signUpIsNotEmpty(email, password, username)
        assertEquals(false, operationSuccess)
    }

    @Test
    fun testWithEmptyPassword(){

        var email = "hiuwenxuan@gmail.com"
        var password  = "" // empty password
        var username = "Leox"

        val operationSuccess : Boolean = signUpIsNotEmpty(email, password, username)
        assertEquals(false, operationSuccess)
    }

    @Test
    fun testWithEmptyUsername(){

        var email = "hiuwenxuan@gmail.com"
        var password  = "123123"
        var username = "" // empty usernmae

        val operationSuccess : Boolean = signUpIsNotEmpty(email, password, username)
        assertEquals(false, operationSuccess)
    }

    @Test
    fun testWithAllEmpty(){

        var email = "" // empty email
        var password  = "" // empty password
        var username = ""

        val operationSuccess : Boolean = signUpIsNotEmpty(email, password, username)
        assertEquals(false, operationSuccess)
    }

    @Test
    fun testWithNoEmpty(){

        var email = "hiuwenxuan@gmail.com" // empty email
        var password  = "123123" // empty password
        var username = "Leox"

        val operationSuccess : Boolean = signUpIsNotEmpty(email, password, username)
        assertEquals(true, operationSuccess)
    }

    @Test
    fun testWithValidPassword(){

        var password = "123123" // valid password

        val operationSuccess : Boolean = validatePassword(password)
        assertEquals(true, operationSuccess)
    }

    @Test
    fun testWithInvalidPassword(){

        var password = "1223" // invalid password

        val operationSuccess : Boolean = validatePassword(password)
        assertEquals(false, operationSuccess)
    }

    private fun signUpIsNotEmpty(email : String, password : String, username : String) : Boolean {

        return email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()
    }

    private fun validatePassword(password : String): Boolean {

        return password.isNotEmpty() && password.length >= 6

    }
}