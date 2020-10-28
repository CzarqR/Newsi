package com.myniprojects.newsi.utils

import timber.log.Timber

fun Any?.logD()
{
    Timber.d(this?.toString() ?: "Object is null")
}

fun Any?.logE()
{
    Timber.e(this?.toString() ?: "Object is null")
}

fun Any?.logI()
{
    Timber.i(this?.toString() ?: "Object is null")
}

fun Any?.logW()
{
    Timber.w(this?.toString() ?: "Object is null")
}

fun Any?.logWTF()
{
    Timber.wtf(this?.toString() ?: "Object is null")
}

fun Any?.logV()
{
    Timber.v(this?.toString() ?: "Object is null")
}

