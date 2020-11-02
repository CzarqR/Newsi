package com.myniprojects.newsi.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat

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