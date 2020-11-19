package com.myniprojects.newsi.utils

import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
import com.myniprojects.newsi.R
import com.myniprojects.newsi.utils.Constants.FORMATTER_LOCAL
import com.myniprojects.newsi.utils.Constants.FORMATTER_NETWORK
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imgUrl: String?)
{
    if (imgUrl == null)
    {
        setImageDrawable(null)
    }
    else
    {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()


        Glide.with(context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_anim)
                    .error(R.drawable.ic_baseline_broken_image_24)
            )
            .listener(
                object : RequestListener<Drawable?>
                {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean
                    {
                        this@bindImage.maxHeight = this@bindImage.context.dpToPx(50f).toInt()
                        this@bindImage.scaleType = ImageView.ScaleType.FIT_CENTER

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean
                    {
                        this@bindImage.maxHeight = this@bindImage.context.dpToPx(250f).toInt()
                        this@bindImage.scaleType = ImageView.ScaleType.CENTER_CROP

                        return false
                    }

                })
            .into(this)
    }

}

@BindingAdapter("htmlText")
fun TextView.bindText(text: String?)
{
    if (text == null)
    {
        this.text = ""
    }
    else
    {
        this.text = text.toSpannedHtml()
    }
}

@BindingAdapter("likedButton")
fun MaterialButton.setSelection(isLiked: Boolean?)
{
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
    // todo validation, some news have different date format
    if (date != null)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            try
            {
                this.text = LocalDateTime.parse(date, FORMATTER_NETWORK).format(FORMATTER_LOCAL)
            }
            catch (_: Exception)
            {
                this.text = date // date couldn't be parsed, api provided it in different format
            }
        }
        else
        {

            val d = SimpleDateFormat(Constants.NETWORK_DATE_FORMAT, Locale.getDefault()).parse(date)
            if (d != null)
            {
                try
                {
                    val formatterLocal = SimpleDateFormat(
                        Constants.LOCAL_DATE_FORMAT,
                        Locale.getDefault()
                    )
                    this.text = formatterLocal.format(d)
                }
                catch (_: Exception)
                {
                    this.text = date // date couldn't be parsed, api provided it in different format
                }

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