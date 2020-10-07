package com.roquebuarque.architecturecomponentssample

import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.roquebuarque.architecturecomponentssample.feature.HostActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith


@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class CountryListFragmentTest {

    @Test
    fun testActivityInitialState() {
        //Setup
        ActivityScenario.launch(HostActivity::class.java)

        //Verify
        onView(withId(R.id.edtSearchCountry)).check(matches(isDisplayed()))

        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText("Countries")))


    }
}