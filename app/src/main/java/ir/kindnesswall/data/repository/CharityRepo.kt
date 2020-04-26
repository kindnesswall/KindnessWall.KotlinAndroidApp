package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.local.dao.AppDatabase
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.remote.network.CharityApi
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
class CharityRepo(val context: Context, var charityApi: CharityApi, var appDatabase: AppDatabase) :
    BaseDataSource() {
    fun getCharityFirstPage(
        viewModelScope: CoroutineScope
    ): LiveData<CustomResult<List<CharityModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            fun fetchFromDb() = appDatabase.charityDao().getAll().map { CustomResult.success(it) }

            emit(CustomResult.loading())

            emitSource(fetchFromDb())

            getResultWithExponentialBackoffStrategy {
                charityApi.getGiftsFirstPage()
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.message.toString()))
                        } else {
                            appDatabase.charityDao().insert(result.data)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.message.toString()))
                }
            }
        }

    fun getCharities(
        viewModelScope: CoroutineScope,
        lastId: Long
    ): LiveData<CustomResult<List<CharityModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            fun fetchFromDb() = appDatabase.charityDao().getAll().map { CustomResult.success(it) }

            emit(CustomResult.loading())

            emitSource(fetchFromDb())

            getResultWithExponentialBackoffStrategy {
                charityApi.getGifts()
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.message.toString()))
                        } else {
                            appDatabase.charityDao().insert(result.data)
                            emitSource(fetchFromDb())
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.message.toString()))
                }
            }
        }
}