package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.Either
import ir.kindnesswall.data.model.ReportCharityMessageModel
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

            when (val result = charityApi.getCharities()) {
                is Either.Right -> {
                    emitSource(MutableLiveData<List<CharityModel>>().apply {
                        value = result.right
                    }.map { CustomResult.success(it) })
                }
                is Either.Left -> {
                    emit(CustomResult.error(
                        CustomResult.ErrorMessage(message = result.left.message,
                            code = result.left.code,
                            errorBody = result.left.errorBody)
                    ))
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
        charityReportMessageModel: ReportCharityMessageModel
    ): LiveData<CustomResult<Any?>> =
        liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getNullableResultWithExponentialBackoffStrategy {
                charityApi.sendReport(charityReportMessageModel)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emit(CustomResult.success(result.data))
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }
}