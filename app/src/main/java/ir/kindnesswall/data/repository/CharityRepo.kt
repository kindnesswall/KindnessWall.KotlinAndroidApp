package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.ReportMessageModel
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
class CharityRepo(context: Context, var charityApi: CharityApi) : BaseDataSource(context) {

    fun getCharities(viewModelScope: CoroutineScope): LiveData<CustomResult<List<CharityModel>>> =
        liveData<CustomResult<List<CharityModel>>>(
            viewModelScope.coroutineContext,
            timeoutInMs = 0
        ) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy { charityApi.getCharities() }
                .collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            if (result.data == null) {
                                emit(CustomResult.error(result.errorMessage))
                            } else {
                                emitSource(MutableLiveData<List<CharityModel>>().apply {
                                    value = result.data
                                }.map { CustomResult.success(it) })
                            }
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                        else -> emit(CustomResult.error(result.errorMessage))
                    }
                }
        }

    fun getCharity(viewModelScope: CoroutineScope, charityId: Long):
            LiveData<CustomResult<CharityModel>> =
        liveData<CustomResult<CharityModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy { charityApi.getCharity(charityId) }
                .collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            if (result.data == null) {
                                emit(CustomResult.error(result.errorMessage))
                            } else {
                                emitSource(MutableLiveData<CharityModel>().apply {
                                    value = result.data
                                }
                                    .map { CustomResult.success(it) })
                            }
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                        else -> emit(CustomResult.error(result.errorMessage))
                    }
                }
        }

    fun sendMessageCharityReport(
        viewModelScope: CoroutineScope,
        charityReportMessageModel: ReportMessageModel
    ):
            LiveData<CustomResult<Any>> =
        liveData<CustomResult<Any>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            getResultWithExponentialBackoffStrategy {
                charityApi.sendReport(
                    charityReportMessageModel
                )
            }
                .collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            if (result.data == null) {
                                emit(CustomResult.error(result.errorMessage))
                            } else {
                                emitSource(MutableLiveData<Any>().apply {
                                    value = result.data
                                }
                                    .map { CustomResult.success(it) })
                            }
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                        else -> emit(CustomResult.error(result.errorMessage))
                    }
                }
        }
}