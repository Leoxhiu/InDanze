package com.example.indanze.activity


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.indanze.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SignUpTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SignUpActivity::class.java)

    @Test
    fun signUpTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.txt_usernameSignUp),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.usernameLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("NewAccount"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.txt_email_signup),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.emaillayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("testing999@gmail.com"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.btn_SignUp), withText("Sign Up"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.txt_password_signup),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.passwordLayout),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("123123"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.btn_SignUp), withText("Sign Up"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
