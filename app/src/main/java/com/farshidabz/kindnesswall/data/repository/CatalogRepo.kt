package com.farshidabz.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.farshidabz.kindnesswall.data.local.dao.AppDatabase
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.BaseDataSource
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.requestsmodel.GetGiftsRequestBody
import com.farshidabz.kindnesswall.data.remote.network.CatalogApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class CatalogRepo(
    val context: Context,
    private val catalogApi: CatalogApi,
    private val appDatabase: AppDatabase
) : BaseDataSource() {

    fun getGiftsFirstPage(
        viewModelScope: CoroutineScope
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            fun fetchFromDb() = appDatabase.catalogDao().getAll().map { CustomResult.success(it) }

            emit(CustomResult.loading())

            emitSource(fetchFromDb())

            getResultWithExponentialBackoffStrategy {
                catalogApi.getGiftsFirstPage(GetGiftsRequestBody())
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(""))
                        } else {
                            appDatabase.catalogDao().insert(result.data)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(""))
                }
            }
        }

    fun getGifts(
        viewModelScope: CoroutineScope,
        lastId: Long
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            fun fetchFromDb() = appDatabase.catalogDao().getAll().map { CustomResult.success(it) }

            emit(CustomResult.loading())

            emitSource(fetchFromDb())

            getResultWithExponentialBackoffStrategy {
                catalogApi.getGifts(GetGiftsRequestBody(lastId))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(""))
                        } else {
                            appDatabase.catalogDao().insert(result.data)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(""))
                }
            }
        }

    fun searchForGiftFirstPage(viewModelScope: CoroutineScope): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            fun fetchFromDb() = appDatabase.catalogDao().getAll().map { CustomResult.success(it) }

            emit(CustomResult.loading())

            emitSource(fetchFromDb())

            getResultWithExponentialBackoffStrategy {
                catalogApi.getGiftsFirstPage(GetGiftsRequestBody())
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(""))
                        } else {
                            appDatabase.catalogDao().insert(result.data)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(""))
                }
            }
        }

    fun searchForGifts(
        viewModelScope: CoroutineScope,
        lastId: Long
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            fun fetchFromDb() = appDatabase.catalogDao().getAll().map { CustomResult.success(it) }

            emit(CustomResult.loading())

            emitSource(fetchFromDb())

            getResultWithExponentialBackoffStrategy {
                catalogApi.getGifts(GetGiftsRequestBody(lastId))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(""))
                        } else {
                            appDatabase.catalogDao().insert(result.data)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(""))
                }
            }
        }
}