package com.myniprojects.newsi.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.myniprojects.newsi.R
import com.myniprojects.newsi.utils.Constants.FORMATTER_SEPARATOR
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

fun String.toSpannedHtml(): Spanned
{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
    {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    }
    else
    {
        HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun String.isDateTheSame(date: String): Boolean
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    {
        try
        {
            val d1 = LocalDateTime.parse(
                this,
                Constants.FORMATTER_NETWORK
            )

            val d2 = LocalDateTime.parse(
                date,
                Constants.FORMATTER_NETWORK
            )

            return d1.year == d2.year && d1.dayOfYear == d2.dayOfYear

        }
        catch (_: Exception)
        {
            return true
        }
    }
    else
    {

        try
        {
            val d1 = SimpleDateFormat(
                Constants.NETWORK_DATE_FORMAT,
                Locale.getDefault()
            ).parse(this)

            val d2 = SimpleDateFormat(
                Constants.NETWORK_DATE_FORMAT,
                Locale.getDefault()
            ).parse(date)

            return if (d1 != null && d2 != null)
            {
                val cal1 = Calendar.getInstance()
                val cal2 = Calendar.getInstance()
                cal1.time = d1
                cal2.time = d2
                cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
                        cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
            }
            else
            {
                true
            }
        }
        catch (_: Exception)
        {
            return true
        }
    }
}


fun String.getDateFormatted(context: Context): String
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    {
        val d = LocalDateTime.parse(
            this,
            Constants.FORMATTER_NETWORK
        )

        val t = LocalDateTime.now()

        return when (d.until(t, ChronoUnit.DAYS))
        {
            0L ->
            {
                context.getString(R.string.today)
            }
            1L ->
            {
                context.getString(R.string.yesterday)
            }
            else ->
            {
                return d.format(FORMATTER_SEPARATOR)
            }
        }
    }
    else
    {
        val d = SimpleDateFormat(
            Constants.NETWORK_DATE_FORMAT,
            Locale.getDefault()
        ).parse(this)

        if (d != null)
        {
            val t = Date()
            val cd = Calendar.getInstance()
            val ct = Calendar.getInstance()
            cd.time = d
            ct.time = t


            if (
                ct.get(Calendar.DAY_OF_YEAR) == cd.get(Calendar.DAY_OF_YEAR) &&
                ct.get(Calendar.YEAR) == cd.get(Calendar.YEAR)
            )
            {
                return context.getString(R.string.today)
            }

            ct.add(Calendar.DAY_OF_YEAR, -1)

            if (
                ct.get(Calendar.DAY_OF_YEAR) == cd.get(Calendar.DAY_OF_YEAR) &&
                ct.get(Calendar.YEAR) == cd.get(Calendar.YEAR)
            )
            {
                return context.getString(R.string.yesterday)
            }

            return SimpleDateFormat(
                Constants.SEPARATOR_FORMAT,
                Locale.getDefault()
            ).format(d)

        }
        else
        {
            throw IllegalArgumentException()
        }
    }
}
