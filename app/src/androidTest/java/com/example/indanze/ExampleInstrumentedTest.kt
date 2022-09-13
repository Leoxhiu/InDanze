package com.example.indanze

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.indanze.activity.SignInActivity
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    val rule : ActivityTestRule<SignInActivity> = ActivityTestRule(SignInActivity::class.java)

    private lateinit var email: String
    private lateinit var password: String
    @Before
    fun setup() {
        email = "hiuwenxuan@gmail.com"
        password = "123123"
    }

    @Test
    fun userTypeEmail(){

        onView(withId(R.id.txt_email)).perform(typeText(email))
        Espresso.closeSoftKeyboard()
    }

    @Test
    fun userTypePassword(){

        onView(withId(R.id.txt_password)).perform(typeText(password))
        Espresso.closeSoftKeyboard()
    }

    @Test
    fun userPerformSignIn(){

        onView(withId(R.id.txt_email)).perform(typeText(email))
        onView(withId(R.id.txt_password)).perform(typeText(password))
        onView(withId(R.id.btn_SignIn)).perform(click())

    }


}