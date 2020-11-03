package com.myniprojects.newsi.utils

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.myniprojects.newsi.R
import com.myniprojects.newsi.utils.Constants.FORMATTER_LOCAL
import com.myniprojects.newsi.utils.Constants.FORMATTER_NETWORK
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imgUrl: String?)
{
    if (imgUrl == null)
    {
        Timber.d("Image url null")
        setImageDrawable(null)
    }
    else
    {
        Timber.d("Not null url $imgUrl")
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUri)
            .into(this)
    }

}

@BindingAdapter("htmlText")
fun TextView.bindText(text: String?)
{
    text?.let {
        this.text = it.toSpannedHtml()
    }
}

@BindingAdapter("likedButton")
fun MaterialButton.setSelection(isLiked: Boolean?)
{
    Timber.d("Liked binding $isLiked")
    if (isLiked != null && isLiked)
    {
        iconTint = ContextCompat.getColorStateList(
            context,
            R.color.favourite_selected
        )
        icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24)
    }
    else
    {
        iconTint = ContextCompat.getColorStateList(
            context,
            R.color.favourite_unselected
        )
        icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
    }
}

@BindingAdapter("timeFormat")
fun TextView.setTimeFormat(date: String?)
{
    if (date != null)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            this.text = LocalDateTime.parse(date, FORMATTER_NETWORK).format(FORMATTER_LOCAL)
        }
        else
        {

            val d = SimpleDateFormat(Constants.NETWORK_DATE_FORMAT, Locale.getDefault()).parse(date)
            if (d != null)
            {
                val formatterLocal = SimpleDateFormat(
                    Constants.LOCAL_DATE_FORMAT,
                    Locale.getDefault()
                )
                this.text = formatterLocal.format(d)
            }
            else
            {
                this.text = ""
            }

        }
    }
    else
    {
        this.text = ""
    }
}