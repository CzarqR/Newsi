package com.myniprojects.newsi.utils

import android.os.Build
import androidx.annotation.StringRes
import com.myniprojects.newsi.R
import java.time.format.DateTimeFormatter

object Constants
{
    // retrofit
    const val BASE_URL = "https://webit-news-search.p.rapidapi.com/"
    const val HEADER_HOST = "x-rapidapi-host: webit-news-search.p.rapidapi.com"
    const val HEADER_KEY = "x-rapidapi-key: 3afd6a0154mshce0ba6c65a48cb8p18fdf5jsn9a31d07e7509"
    const val NEWS_STARTING_PAGE_INDEX = 0
    const val NETWORK_PAGE_SIZE = 30
    const val INITIAL_LOAD_SIZE = 2

    // date-time format
    const val NETWORK_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val LOCAL_DATE_FORMAT = "HH:mm MMM d"
    const val SEPARATOR_FORMAT = "d MMM"
    val DATE_REGEX = "^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d\$".toRegex()


    val FORMATTER_NETWORK: DateTimeFormatter? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    {
        DateTimeFormatter.ofPattern(NETWORK_DATE_FORMAT)
    }
    else
    {
        null
    }


    val FORMATTER_LOCAL: DateTimeFormatter? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    {
        DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)
    }
    else
    {
        null
    }


    val FORMATTER_SEPARATOR: DateTimeFormatter? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    {
        DateTimeFormatter.ofPattern(SEPARATOR_FORMAT)
    }
    else
    {
        null
    }

    // SharedPreferences
    const val SHARED_PREFERENCES_NAME = "newsi_data_sh"

    val DARK_MODE_SH = "KEY_DARK_MODE" to false
    val OPEN_IN_EXTERNAL_SH = "KEY_OPEN_IN_EXTERNAL" to false
    val FIRST_RUN_SH = "FIRST_RUN_KEY" to true
    val LAST_RUN_SH = "LAST_RUN_KEY" to 0L


    // database
    const val DATABASE_NAME = "newsi.db"

    // WorkManager
    const val REFRESH_WORK_NAME = "REFRESH_DATA_WORKER"
    const val LOAD_AFTER_MINIMUM = 1000 * 60 * 60 * 3 // load fresh news only after 3h after last app run
    const val LOAD_DEFAULT_TIME = 1L // run WorkManager after 12 hours (1 to test)


    // Notifications
    const val CHANNEL_FRESH_NEWS_ID = "chanel_fresh_news_id"
    const val SEARCH_INPUT_KEY = "SEARCH_INPUT_KEY"
    const val FRESH_NEWS_NOTIFICATION_ID = 13

    private const val HOT_NEWS_KEY = "HOT_NEWS_KEY"

    @StringRes
    private const val HOT_NEWS_NAME = R.string.hot_news_notification

    val HOT_NEWS = HOT_NEWS_KEY to HOT_NEWS_NAME
}

