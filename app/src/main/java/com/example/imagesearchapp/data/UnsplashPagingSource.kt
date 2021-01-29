package com.example.imagesearchapp.data

import android.util.Log
import androidx.paging.PagingSource
import com.example.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

    companion object {
        const val TAG = "UnsplashPagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val currentPosition = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.searchPhotos(
                query = query,
                page = currentPosition,
                perPage = params.loadSize
            )

            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (currentPosition == UNSPLASH_STARTING_PAGE_INDEX) null else currentPosition - 1,
                nextKey = if (photos.isEmpty()) null else currentPosition + 1
            )
        } catch (exception: IOException) { // No internet connection
            Log.e(TAG, exception.message.toString())
            LoadResult.Error(exception)
        } catch (exception: HttpException) { // Some error occurred in the request made
            Log.e(TAG, exception.message.toString())
            LoadResult.Error(exception)
        }
    }
}