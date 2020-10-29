package com.myniprojects.newsi.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.newsi.network.data.ApiResult
import com.myniprojects.newsi.repository.MainRepository
import com.myniprojects.newsi.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel()
{

    private val _dataState: MutableLiveData<DataState<ApiResult>> = MutableLiveData()
    val dataState: LiveData<DataState<ApiResult>>
        get() = _dataState

    fun loadTrendingNews()
    {
        viewModelScope.launch(Dispatchers.IO) {

            mainRepository.getTrendingNews().onEach {
                _dataState.value = it
            }.launchIn(viewModelScope)
        }
    }

}