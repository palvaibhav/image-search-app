package com.example.imagesearchapp.data

import androidx.paging.PagingSource
import com.example.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

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
            LoadResult.Error(exception)
        } catch (exception: HttpException) { // Some error occurred in the request made
            LoadResult.Error(exception)
        }
    }
}