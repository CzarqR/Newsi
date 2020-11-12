package com.myniprojects.newsi.repository

import androidx.paging.PagingSource
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.network.data.NetworkToDomainMapper
import com.myniprojects.newsi.utils.Constants.NEWS_STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

// old adapter for single source, learning purpose
@Suppress("unused")
class NewsPagingSource(
    private val newsRetrofit: NewsRetrofit,
    private val networkToDomainMapper: NetworkToDomainMapper
) : PagingSource<Int, News>()
{
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News>
    {
        val position = params.key ?: NEWS_STARTING_PAGE_INDEX
        return try
        {
            val response = newsRetrofit.getTrending(params.loadSize, position * params.loadSize)
            val news = networkToDomainMapper.mapToNewModelList(response.data.results)

            LoadResult.Page(
                data = news,
                prevKey = if (position == NEWS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (news.isEmpty()) null else position + 1
            )
        }
        catch (exception: IOException)
        {
            return LoadResult.Error(exception)
        }
        catch (exception: HttpException)
        {
            return LoadResult.Error(exception)
        }
    }

}