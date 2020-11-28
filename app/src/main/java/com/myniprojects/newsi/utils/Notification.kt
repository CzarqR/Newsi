package com.myniprojects.newsi.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import com.myniprojects.newsi.R
import com.myniprojects.newsi.ui.MainActivity
import com.myniprojects.newsi.utils.Constants.CHANNEL_FRESH_NEWS_ID
import com.myniprojects.newsi.utils.Constants.HOT_NEWS
import com.myniprojects.newsi.utils.Constants.SEARCH_INPUT_KEY

fun Context.createFreshNewsNotification(): Notification
{
    val openResultIntent = Intent(this, MainActivity::class.java)
    val openPendingIntent: PendingIntent = PendingIntent.getActivity(
        this, 0, openResultIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    val searchInput: RemoteInput = RemoteInput.Builder(SEARCH_INPUT_KEY).run {
        setLabel(getString(R.string.fresh_news_hint))
        build()
    }

    val searchAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
        0,
        getString(R.string.find_news),
        openPendingIntent
    ).addRemoteInput(searchInput)
        .build()

    val openHotIntent = Intent(this, MainActivity::class.java).putExtra(
        HOT_NEWS.first,
        getString(HOT_NEWS.second)
    )
    val openHotPendingIntent: PendingIntent = PendingIntent.getActivity(
        this, 1, openHotIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )


    val openHotAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
        0,
        getString(HOT_NEWS.second),
        openHotPendingIntent
    ).build()

    return NotificationCompat.Builder(this, CHANNEL_FRESH_NEWS_ID)
        .setContentTitle(getString(R.string.fresh_news))
        .setContentText(getString(R.string.fresh_news_content))
        .setSmallIcon(R.drawable.ic_baseline_update_24)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_EVENT)
        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .setAutoCancel(true)
        .setContentIntent(openPendingIntent)
        .addAction(openHotAction)
        .addAction(searchAction)
        .build()
}