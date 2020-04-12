package com.farshidabz.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.farshidabz.kindnesswall.data.local.dao.AppDatabase
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.BaseDataSource
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.requestsmodel.GetGiftsRequestBaseBody
import com.farshidabz.kindnesswall.data.model.requestsmodel.RegisterGiftRequestModel
import com.farshidabz.kindnesswall.data.remote.network.GiftApi
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

class GiftRepo(
    val context: Context,
    private val giftApi: GiftApi,
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
                giftApi.getGiftsFirstPage(GetGiftsRequestBaseBody())
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.message.toString()))
                        } else {
                            appDatabase.catalogDao().insert(result.data)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.message.toString()))
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
                giftApi.getGifts(GetGiftsRequestBaseBody().apply { beforeId = lastId })
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

    fun searchForGiftFirstPage(
        viewModelScope: CoroutineScope,
        getGiftsRequestBody: GetGiftsRequestBaseBody
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                giftApi.getGiftsFirstPage(getGiftsRequestBody)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(""))
                        } else {
                            emitSource(MutableLiveData<List<GiftModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(""))
                }
            }
        }

    fun searchForGifts(
        viewModelScope: CoroutineScope,
        lastId: Long,
        getGiftsRequestBody: GetGiftsRequestBaseBody
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                giftApi.getGifts(getGiftsRequestBody.apply { beforeId = lastId })
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(""))
                        } else {
                            emitSource(MutableLiveData<List<GiftModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(""))
                }
            }
        }


    fun registerGift(
        viewModelScope: CoroutineScope,
        registerGiftRequestModel: RegisterGiftRequestModel
    ): LiveData<CustomResult<GiftModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                giftApi.registerGift(registerGiftRequestModel)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.message))
                        } else {
                            emitSource(MutableLiveData<GiftModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.message))
                }
            }
        }
}