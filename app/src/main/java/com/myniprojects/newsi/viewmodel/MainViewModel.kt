package com.myniprojects.newsi.viewmodel

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myniprojects.livesh.liveData
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.repository.NewsRepository
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.DataState
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val newsRepository: NewsRepository,
    val sharedPreferences: SharedPreferences
) : ViewModel()
{

    private var currentSearchResult: Flow<PagingData<News>>? = null

    fun searchNews(): Flow<PagingData<News>>
    {
        val lastResult = currentSearchResult

        if (lastResult != null)
        {
            return lastResult
        }

        val newResult: Flow<PagingData<News>> = newsRepository.getSearchResultStream()
            .cachedIn(viewModelScope)

        currentSearchResult = newResult
        return newResult
    }


//    private val _dataStateTrending: MutableLiveData<DataState<List<News>>> = MutableLiveData()
//    private val dataStateTrending: LiveData<DataState<List<News>>>
//        get() = _dataStateTrending
//
//    private val _dataStateSearch: MutableLiveData<DataState<List<News>>> = MutableLiveData()
//    private val dataStateSearch: LiveData<DataState<List<News>>>
//        get() = _dataStateSearch
//


    private val _loadedNews: MutableLiveData<DataState<List<News>>> = MutableLiveData()
    val loadedNews: LiveData<DataState<List<News>>>
        get() = _loadedNews

//    val news = mainRepository.news

//    val news = MediatorLiveData<DataState<List<News>>>()

    val darkMode = sharedPreferences.liveData(DARK_MODE_SH)

    var searchText: String? = null
        set(value)
        {
            field = value
//            if (value == null)
//            {
//                news.value = dataStateTrending.value
//            }
//            else
//            {
//                loadSearchedNews(value)
//                news.value = dataStateSearch.value
//            }
        }

    init
    {
//        news.addSource(dataStateTrending) { result ->
//            if (searchText == null)
//            {
//                result?.let {
//                    news.value = it
//                }
//            }
//        }
//
//        news.addSource(dataStateSearch) { result ->
//            if (searchText != null)
//            {
//                result?.let {
//                    news.value = it
//                }
//            }
//        }


//        loadTrendingNews()
//        _dataStateSearch.value = DataState.Loading
    }


//    fun loadTrendingNews()
//    {
//        viewModelScope.launch {
//            mainRepository.getTrendingNewsNetwork().onEach {
//                _loadedNews.postValue(it)
//            }.launchIn(viewModelScope + Dispatchers.IO)
//        }
//    }

//    private fun loadSearchedNews(
//        searchText: String,
//        number: Int = DEFAULT_LOADING_NUMBER,
//        offset: Int = 0
//    )
//    {
//        viewModelScope.launch {
//            mainRepository.getSearchedNews(searchText, number, offset).onEach {
//                _dataStateSearch.postValue(it)
//            }.launchIn(viewModelScope + Dispatchers.IO)
//        }
//    }

    fun likeNews(id: String)
    {
        Timber.d("Like $id")
    }

    fun openNews(news: News)
    {
        Timber.d("Open news $news")
//        _openedNews.value = news
    }

//    fun refresh()
//    {
//        loadTrendingNews()
//    }

}