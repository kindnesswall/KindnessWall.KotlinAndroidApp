package com.farshidabz.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.farshidabz.kindnesswall.data.local.dao.AppDatabase
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceModel
import com.farshidabz.kindnesswall.data.model.BaseDataSource
import com.farshidabz.kindnesswall.data.model.CategoryModel
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.remote.network.GeneralApi
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
class GeneralRepo(val context: Context, var generalApi: GeneralApi, var appDatabase: AppDatabase) :
    BaseDataSource() {

    fun getProvinces(viewModelScope: CoroutineScope): LiveData<CustomResult<List<ProvinceModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            fun fetchFromDb() = appDatabase.provinceDao().getAll().map { CustomResult.success(it) }

            emit(CustomResult.loading())

            emitSource(fetchFromDb())

            getResultWithExponentialBackoffStrategy {
                generalApi.getProvinces()
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(""))
                        } else {
                            appDatabase.provinceDao().insert(result.data)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(""))
                }
            }
        }

    fun getCities(
        viewModelScope: CoroutineScope,
        provinceId: Int
    ): LiveData<CustomResult<List<CityModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                generalApi.getCities(provinceId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(""))
                        } else {
                            emitSource(MutableLiveData<List<CityModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(""))
                }
            }
        }

    fun getAllCatgories(viewModelScope: CoroutineScope): LiveData<CustomResult<List<CategoryModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy { generalApi.getAllCategories() }
                .collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            if (result.data == null) {
                                emit(CustomResult.error(""))
                            } else {
                                emitSource(MutableLiveData<List<CategoryModel>>().apply {
                                    value = result.data
                                }.map { CustomResult.success(it) })
                            }
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                        else -> emit(CustomResult.error(""))
                    }
                }
        }
}