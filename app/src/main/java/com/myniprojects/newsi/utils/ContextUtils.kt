package com.myniprojects.newsi.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment

fun Context.hideKeyboard(view: View)
{
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.hideKeyboard()
{
    if (this.window != null)
    {
        val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)

        //remove focus from EditText
        findViewById<View>(android.R.id.content).clearFocus()
    }
}

fun Fragment.hideKeyboard()
{
    requireActivity().hideKeyboard()
}

fun Fragment.setActivityTitle(@StringRes id: Int)
{
    (activity as AppCompatActivity?)!!.supportActionBar?.title =
        getString(id)
}

fun Fragment.setActivityTitle(title: String)
{
    (activity as AppCompatActivity?)!!.supportActionBar?.title = title
}

fun Fragment.openWeb(url: String): Boolean
{
    val browserIntent =
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
    return if (browserIntent.resolveActivity(requireActivity().packageManager) == null)
    {
        false
    }
    else
    {
        startActivity(browserIntent)
        true
    }
}

fun Activity.getShareIntent(text: String): Intent
{
    return ShareCompat.IntentBuilder.from(this)
        .setText(text)
        .setType("text/plain").intent
}

fun Fragment.getShareIntent(text: String): Intent
{
    return requireActivity().getShareIntent(text)
}


fun Context.pxToDp(px: Float): Float
{
    return px / resources.displayMetrics.density
}

fun Context.dpToPx(dp: Float): Float
{
    return dp * resources.displayMetrics.density
}