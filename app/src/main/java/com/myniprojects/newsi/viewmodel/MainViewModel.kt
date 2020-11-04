package com.myniprojects.newsi.viewmodel

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.newsi.model.News
import com.myniprojects.newsi.repository.MainRepository
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.DataState
import com.myniprojects.newsi.utils.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    val sharedPreferences: SharedPreferences
) : ViewModel()
{
    private val _dataState: MutableLiveData<DataState<List<News>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<News>>>
        get() = _dataState

    private val _openedNews: MutableLiveData<News> = MutableLiveData()
    val openedNews: LiveData<News>
        get() = _openedNews

    val darkMode = sharedPreferences.liveData(DARK_MODE_SH)

    init
    {
        loadTrendingNews()
    }


    private fun loadTrendingNews()
    {
        viewModelScope.launch {
            mainRepository.getTrendingNews().onEach {
                _dataState.postValue(it)
            }.launchIn(viewModelScope + Dispatchers.IO)
        }
    }

    fun likeNews(id: String)
    {
        Timber.d("Like $id")
    }

    fun openNews(news: News)
    {
        Timber.d("Open news $news")
        _openedNews.value = news
    }

}