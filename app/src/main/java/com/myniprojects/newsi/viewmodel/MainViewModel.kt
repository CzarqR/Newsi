package com.myniprojects.newsi.viewmodel

import android.content.SharedPreferences
import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.myniprojects.livesh.liveData
import com.myniprojects.newsi.adapters.newsrecycler.NewsRecyclerModel
import com.myniprojects.newsi.db.AppDatabase
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.repository.NewsRepository
import com.myniprojects.newsi.utils.Constants
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.Constants.DATE_REGEX
import com.myniprojects.newsi.utils.Constants.OPEN_IN_EXTERNAL_SH
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
    lateinit var openedNews: News
        private set

    private var currentSearchResult: Flow<PagingData<NewsRecyclerModel>>? = null


    var currentKey: String? = null
        private set

    var submittedKey: String? = null

    init
    {
        sharedPreferences.edit()
            .putLong(Constants.LAST_RUN_SH.first, System.currentTimeMillis())
            .apply()
    }

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
            .map { pagingData ->
                pagingData.filter { news ->
                    news.desc != null && DATE_REGEX.matches(
                        news.date
                    )

                }
            }
            .map { pagingData ->
                pagingData.map { news ->
                    news.isLiked = appDatabase.domainNewsDao.checkIfLiked(
                        news.url
                    ) > 0
                    news
                }
            }
            .map { pagingData -> pagingData.map { NewsRecyclerModel.NewsItem(it) } }
            .insertDateSeparators()
            .cachedIn(viewModelScope)

        PagingData
        currentSearchResult = newResult
        return newResult
    }

    val darkMode = sharedPreferences.liveData(DARK_MODE_SH)
    val openInExternal = sharedPreferences.liveData(OPEN_IN_EXTERNAL_SH)

    fun openInExternalValue() =
        sharedPreferences.getBoolean(OPEN_IN_EXTERNAL_SH.first, OPEN_IN_EXTERNAL_SH.second)

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

    fun likeOpenedNews()
    {
        Timber.d("Like opened news")

        openedNews.isLiked = !openedNews.isLiked

        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.newsDao.changeLike(openedNews.isLiked, openedNews.url)

            if (openedNews.isLiked)
            {
                appDatabase.domainNewsDao.insert(openedNews)
            }
            else
            {
                appDatabase.domainNewsDao.delete(openedNews.url)
            }
        }
    }

    fun openNews(news: News)
    {
        Timber.d("Open news $news")
        openedNews = news
    }

    val likedNews = newsRepository.getLikedNews().mapToRecyclerModel().cachedIn(viewModelScope)
    val countLikeNews = appDatabase.domainNewsDao.countLiked()


    private fun Flow<PagingData<News>>.mapToRecyclerModel(): Flow<PagingData<NewsRecyclerModel>>
    {
        return this.map { pagingData -> pagingData.filter { news -> news.desc != null } }
            .map { pagingData -> pagingData.map { NewsRecyclerModel.NewsItem(it) } }
            .insertDateSeparators()

    }

    private fun Flow<PagingData<NewsRecyclerModel.NewsItem>>.insertDateSeparators(): Flow<PagingData<NewsRecyclerModel>>
    {
        return map {
            it.insertSeparators { before, after ->
                if (after == null)
                {
                    // we're at the end of the list
                    return@insertSeparators null
                }

                if (before == null)
                {
                    // we're at the beginning of the list
                    return@insertSeparators NewsRecyclerModel.SeparatorItem(after.news.date)
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
    }


    fun saveHomeScrollPosition(onSaveInstanceState: Parcelable?)
    {
        _scrollPosHome.value = onSaveInstanceState
    }

    private val _scrollPosHome: MutableLiveData<Parcelable?> = MutableLiveData<Parcelable?>()
    val scrollPosHome: LiveData<Parcelable?>
        get() = _scrollPosHome


    fun saveLikedScrollPosition(onSaveInstanceState: Parcelable?)
    {
        _scrollPosLiked.value = onSaveInstanceState
    }

    fun initInput(inputText: String?)
    {
        Timber.d(inputText)
    }

    private val _scrollPosLiked: MutableLiveData<Parcelable?> = MutableLiveData<Parcelable?>()
    val scrollPosLiked: LiveData<Parcelable?>
        get() = _scrollPosLiked
}

