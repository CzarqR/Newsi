package com.myniprojects.newsi

import android.content.Context
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.myniprojects.newsi.utils.Constants.FORMATTER_NETWORK
import com.myniprojects.newsi.utils.Constants.FORMATTER_SEPARATOR
import com.myniprojects.newsi.utils.Constants.NETWORK_DATE_FORMAT
import com.myniprojects.newsi.utils.Constants.SEPARATOR_FORMAT
import com.myniprojects.newsi.utils.getDateFormatted
import com.myniprojects.newsi.utils.isDateTheSame
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest
{

    lateinit var context: Context

    @Before
    fun setup()
    {
        context = getInstrumentation().targetContext.applicationContext
    }


    @Test
    fun useAppContext()
    {
        // Context of the app under test.
        val appContext = getInstrumentation().targetContext
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val d = java.time.LocalDateTime.now()
            val s = d.format(FORMATTER_NETWORK)
            assert(s.getDateFormatted(context) == context.getString(R.string.today))

            val d2 = java.time.LocalDateTime.now().minusDays(1)
            val s2 = d2.format(FORMATTER_NETWORK)
            assert(s2.getDateFormatted(context) == context.getString(R.string.yesterday))

            val d3 = java.time.LocalDateTime.now().minusDays(2)
            val s3 = d3.format(FORMATTER_NETWORK)
            assert(s3.getDateFormatted(context) == d3.format(FORMATTER_SEPARATOR))
        }
        else
        {
            val sdf = SimpleDateFormat(NETWORK_DATE_FORMAT, Locale.getDefault())
            val today = Date()


            assert(sdf.format(today).getDateFormatted(context) == context.getString(R.string.today))

            today.time -= 1000 * 60 * 60 * 24

            assert(
                sdf.format(today)
                    .getDateFormatted(context) == context.getString(R.string.yesterday)
            )

            today.time -= 1000 * 60 * 60 * 24

            val sdfS = SimpleDateFormat(SEPARATOR_FORMAT, Locale.getDefault())


            assert(sdf.format(today).getDateFormatted(context) == sdfS.format(today))
        }

    }
}