package com.myniprojects.newsi

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myniprojects.newsi.utils.getDateFormatted
import com.myniprojects.newsi.utils.isDateTheSame

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest
{

    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }


    @Test
    fun useAppContext()
    {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.myniprojects.newsi", appContext.packageName)
    }

    @Test
    fun dateComparer()
    {
        val d1 = "2020-11-09 13:41:38"
        val d2 = "2020-11-09 13:40:38"
        val d3 = "2020-11-08 13:41:38"
        assert(d1.isDateTheSame(d2))
        assert(!d1.isDateTheSame(d3))
    }

    @Test
    fun datePrinter()
    {
        val d1 = "2020-11-09 13:41:38"
        assert(d1.getDateFormatted(instrumentationContext) == "Today")
    }
}