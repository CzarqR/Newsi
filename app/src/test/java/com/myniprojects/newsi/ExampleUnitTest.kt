package com.myniprojects.newsi

import com.myniprojects.newsi.utils.isDateTheSame
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest
{
    @Test
    fun addition_isCorrect()
    {
        assertEquals(4, 2 + 2)
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
}