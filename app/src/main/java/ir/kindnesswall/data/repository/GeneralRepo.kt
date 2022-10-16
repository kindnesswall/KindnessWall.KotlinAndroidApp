package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.local.dao.AppDatabase
import ir.kindnesswall.data.local.dao.province.ProvinceModel
import ir.kindnesswall.data.model.*
import ir.kindnesswall.data.remote.network.GeneralApi
import ir.kindnesswall.domain.common.CustomResult
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
class GeneralRepo(context: Context, var generalApi: GeneralApi, var appDatabase: AppDatabase) :
    BaseDataSource(context) {

    fun getProvinces(viewModelScope: CoroutineScope): LiveData<CustomResult<List<ProvinceModel>>> =
        liveData<CustomResult<List<ProvinceModel>>>(
            viewModelScope.coroutineContext,
            timeoutInMs = 0
        ) {
            fun fetchFromDb() = appDatabase.provinceDao().getAll().map { CustomResult.success(it) }

            emit(CustomResult.loading())

            emitSource(fetchFromDb())

            getResultWithExponentialBackoffStrategy {
                generalApi.getProvinces()
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            appDatabase.provinceDao().insert(result.data!!)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun getCities(
        viewModelScope: CoroutineScope,
        provinceId: Int
    ): LiveData<CustomResult<List<CityModel>>> =
        liveData<CustomResult<List<CityModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                generalApi.getCities(provinceId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<List<CityModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun getRegions(
        viewModelScope: CoroutineScope,
        cityId: Int
    ): LiveData<CustomResult<List<RegionModel>>> =
        liveData<CustomResult<List<RegionModel>>>(
            viewModelScope.coroutineContext,
            timeoutInMs = 0
        ) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                generalApi.getRegions(cityId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<List<RegionModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun getAllCatgories(viewModelScope: CoroutineScope): LiveData<CustomResult<List<CategoryModel>>> =
        liveData<CustomResult<List<CategoryModel>>>(
            viewModelScope.coroutineContext,
            timeoutInMs = 0
        ) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy { generalApi.getAllCategories() }
                .collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            if (result.data == null) {
                                emit(CustomResult.error(result.errorMessage))
                            } else {
                                emitSource(MutableLiveData<List<CategoryModel>>().apply {
                                    value = result.data
                                }.map { CustomResult.success(it) })
                            }
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                        else -> emit(CustomResult.error(result.errorMessage))
                    }
                }
        }

    fun getVersion(viewModelScope: CoroutineScope): LiveData<CustomResult<UpdateModel>> =
        liveData<CustomResult<UpdateModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy { generalApi.getVersion() }
                .collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            if (result.data == null) {
                                emit(CustomResult.error(result.errorMessage))
                            } else {
                                emitSource(MutableLiveData<UpdateModel>().apply {
                                    value = result.data
                                }.map { CustomResult.success(it) })
                            }
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                        else -> emit(CustomResult.error(result.errorMessage))
                    }
                }
        }


    fun getSetting(viewModelScope: CoroutineScope): LiveData<CustomResult<SettingModel>> =
        liveData<CustomResult<SettingModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { generalApi.getSetting() }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<SettingModel>().apply {
                            value = result.data
                        }.map { CustomResult.success(it) })

                    }
                }
            }
        }
}