package com.myniprojects.newsi.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object Constants
{
    // retrofit
    const val BASE_URL = "https://webit-news-search.p.rapidapi.com/"
    const val HEADER_HOST = "x-rapidapi-host: webit-news-search.p.rapidapi.com"
    const val HEADER_KEY = "x-rapidapi-key: 3afd6a0154mshce0ba6c65a48cb8p18fdf5jsn9a31d07e7509"

    // date-time format
    const val NETWORK_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val LOCAL_DATE_FORMAT = "HH:mm MMM dd"
    @RequiresApi(Build.VERSION_CODES.O)
    val FORMATTER_NETWORK = DateTimeFormatter.ofPattern(NETWORK_DATE_FORMAT)
    @RequiresApi(Build.VERSION_CODES.O)
    val FORMATTER_LOCAL = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)



}

