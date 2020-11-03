package com.myniprojects.newsi.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.newsi.model.News
import com.myniprojects.newsi.repository.MainRepository
import com.myniprojects.newsi.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import me.ibrahimsn.library.LiveSharedPreferences
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val _liveSharedPreferences: LiveSharedPreferences
) : ViewModel()
{
    private val _dataState: MutableLiveData<DataState<List<News>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<News>>>
        get() = _dataState

    private val _openedNews: MutableLiveData<News> = MutableLiveData()
    val openedNews: LiveData<News>
        get() = _openedNews

    val liveSharedPreferences: LiveSharedPreferences
        get() = _liveSharedPreferences

    init
    {
        loadTrendingNews()
    }


    private fun loadTrendingNews()
    {
        viewModelScope.launch {
            Timber.d("Current thread ${Thread.currentThread().name}")

            mainRepository.getTrendingNews().onEach {
                Timber.d("Current thread ${Thread.currentThread().name}")
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