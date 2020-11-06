package com.myniprojects.newsi.viewmodel

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myniprojects.livesh.liveData
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.repository.MainRepository
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.Constants.DEFAULT_LOADING_NUMBER
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    val sharedPreferences: SharedPreferences
) : ViewModel()
{

//    private val _dataStateTrending: MutableLiveData<DataState<List<News>>> = MutableLiveData()
//    private val dataStateTrending: LiveData<DataState<List<News>>>
//        get() = _dataStateTrending
//
//    private val _dataStateSearch: MutableLiveData<DataState<List<News>>> = MutableLiveData()
//    private val dataStateSearch: LiveData<DataState<List<News>>>
//        get() = _dataStateSearch
//

//    private val _openedNews: MutableLiveData<News> = MutableLiveData()
//    val openedNews: LiveData<News>
//        get() = _openedNews

    val news = mainRepository.news

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


        loadTrendingNews()
//        _dataStateSearch.value = DataState.Loading
    }


    private fun loadTrendingNews(number: Int = DEFAULT_LOADING_NUMBER, offset: Int = 0)
    {
        viewModelScope.launch {

            mainRepository.getNews(number, offset)

//            mainRepository.getTrendingNews(number, offset).onEach {
//                _dataStateTrending.postValue(it)
//            }.launchIn(viewModelScope + Dispatchers.IO)
        }
    }

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

}