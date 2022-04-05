package com.example.bookstore

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.bookstore.view.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.bookstore", appContext.packageName)
    }

    @Test
    fun test_email_one() {
        onView(withId(R.id.logIn_Email)).perform(typeText("rama@gmail.com"))
    }

    @Test
    fun test_email_two() {
        onView(withId(R.id.logIn_Email)).perform(typeText("ramakrishna96.velisetti@gmail.com"))
    }

    @Test
    fun test_password_one() {
        onView(withId(R.id.logIn_Password)).perform(typeText("123456789"))
    }
    @Test
    fun test_password_two() {
        onView(withId(R.id.logIn_Password)).perform(typeText("qwgthjkdl"))
    }
    @Test
    fun test_password_three() {
        onView(withId(R.id.logIn_Password)).perform(typeText("qw98hRLkdl"))
    }
}