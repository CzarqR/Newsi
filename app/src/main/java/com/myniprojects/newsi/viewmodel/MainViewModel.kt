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
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel()
{

    private val _dataState: MutableLiveData<DataState<List<News>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<News>>>
        get() = _dataState

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

    init
    {
        loadTrendingNews()
    }

}