package com.myniprojects.newsi.viewmodel

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.myniprojects.livesh.liveData
import com.myniprojects.newsi.adapters.NewsRecyclerModel
import com.myniprojects.newsi.db.AppDatabase
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.repository.NewsRepository
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.isDateTheSame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val newsRepository: NewsRepository,
    private val appDatabase: AppDatabase,
    val sharedPreferences: SharedPreferences
) : ViewModel()
{
    private val _openedNews = MutableLiveData<News>()
    val openedNews: LiveData<News>
        get() = _openedNews

    private var currentSearchResult: Flow<PagingData<NewsRecyclerModel>>? = null


    var currentKey: String? = null
        private set

    var submittedKey: String? = null


    fun searchNews(): Flow<PagingData<NewsRecyclerModel>>
    {
        Timber.d("Search news with key:$submittedKey")
        val lastResult = currentSearchResult

        if (currentKey == submittedKey && lastResult != null)
        {
            return lastResult
        }

        currentKey = submittedKey

        val newResult: Flow<PagingData<NewsRecyclerModel>> = newsRepository.getSearchResultStream(
            submittedKey
        )
            .map { pagingData -> pagingData.filter { news -> news.desc != null } }
            .map { pagingData -> pagingData.map { NewsRecyclerModel.NewsItem(it) } }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null)
                    {
                        // we're at the end of the list
                        return@insertSeparators null
                    }

                    if (before == null)
                    {
                        // we're at the beginning of the list
                        return@insertSeparators null
                    }

                    // check between 2 items
                    if (!before.news.date.isDateTheSame(after.news.date)) // different date
                    {
                        NewsRecyclerModel.SeparatorItem(after.news.date)
                    }
                    else
                    {
                        null
                    }
                }
            }
            .map {
                it.insertHeaderItem(NewsRecyclerModel.SeparatorItem("2020-10-09 21:10:00")) // todo load first item
            }
            .cachedIn(viewModelScope)
        PagingData
        currentSearchResult = newResult
        return newResult
    }

    val darkMode = sharedPreferences.liveData(DARK_MODE_SH)

    fun likeNews(news: News)
    {
        Timber.d("Like $news")

        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.newsDao.changeLike(!news.isLiked, news.url)

//            news.isLiked = !news.isLiked

            val c = news.copy(isLiked = !news.isLiked)

            if (c.isLiked)
            {
                appDatabase.domainNewsDao.insert(c)
            }
            else
            {
                appDatabase.domainNewsDao.delete(c.url)
            }
        }
    }

    fun openNews(news: News)
    {
        Timber.d("Open news $news")
        _openedNews.value = news
    }

}
