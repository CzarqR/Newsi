package com.myniprojects.newsi.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import timber.log.Timber

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?)
{
    if (imgUrl == null)
    {
        Timber.d("Image url null")
        imgView.setImageDrawable(null)
    }
    else
    {
        Timber.d("Not null url $imgUrl")
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }

}

@BindingAdapter("htmlText")
fun bindText(textView: TextView, text: String?)
{
    text?.let {
        textView.text = it.toSpannedHtml()
    }
}