package com.myniprojects.newsi.utils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import timber.log.Timber

class ObservableSharedPreference<T>(
    private val key: String,
    private val defValue: T,
    private val sharedPrefs: SharedPreferences
) : LiveData<T>()
{

    constructor(
        pair: Pair<String, T>, sharedPrefs: SharedPreferences
    ) : this(pair.first, pair.second, sharedPrefs)

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == this.key)
        {
            value = getValueFromPreferences()
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun getValueFromPreferences(): T
    {
        when (defValue)
        {
            is Boolean ->
            {
                return sharedPrefs.getBoolean(key, defValue) as T
            }
            is Int ->
            {
                return sharedPrefs.getInt(key, defValue) as T
            }
            is Long ->
            {
                return sharedPrefs.getLong(key, defValue) as T
            }
            is String ->
            {
                return sharedPrefs.getString(key, defValue) as T
            }
            is Float ->
            {
                return sharedPrefs.getFloat(key, defValue) as T
            }
            //is Set<String>?->
            //{
            //    return sharedPrefs.getStringSet(key, defValue) as T
            //}
            else ->
            {
                Timber.d("Unreported sharedPref")
                throw IllegalArgumentException()
            }
        }
    }

    override fun onActive()
    {
        super.onActive()
        value = getValueFromPreferences()
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive()
    {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }
}


fun <T> SharedPreferences.liveData(
    key: String,
    defValue: T
): ObservableSharedPreference<T>
{
    return ObservableSharedPreference(key, defValue, this)
}

fun <T> SharedPreferences.liveData(
    pair: Pair<String, T>
): ObservableSharedPreference<T>
{
    return ObservableSharedPreference(pair, this)
}

