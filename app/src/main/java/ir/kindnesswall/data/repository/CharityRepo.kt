package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterCharityModel
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.requestsmodel.RejectGiftRequestModel
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
class CharityRepo(val context: Context, var charityApi: CharityApi) : BaseDataSource() {

    fun getCharities(viewModelScope: CoroutineScope): LiveData<CustomResult<List<CharityModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

    fun getCharity(viewModelScope: CoroutineScope, charityId: Long): LiveData<CustomResult<CharityModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

    fun getUserCharity(viewModelScope: CoroutineScope): LiveData<CustomResult<CharityModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            /*        getResultWithExponentialBackoffStrategy { charityApi.getCharityInfo() }
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
                            }*/
        }

    fun registerCharity(viewModelScope: CoroutineScope, userId: Long, model: RegisterCharityModel): LiveData<CustomResult<CharityModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                charityApi.registerCharity(userId, model)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<CharityModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun updateCharity(viewModelScope: CoroutineScope, userId: Long, model: RegisterCharityModel): LiveData<CustomResult<CharityModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                charityApi.updateCharity(userId, model)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<CharityModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun getReviewCharityFirstPage(viewModelScope: CoroutineScope): LiveData<CustomResult<List<CharityModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                charityApi.getReviewCharityFirstPage()
            }.collect { result ->
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


    fun getReviewCharity(viewModelScope: CoroutineScope): LiveData<CustomResult<List<CharityModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                charityApi.getReviewCharity()
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<List<CharityModel>>().apply { value = result.data }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun rejectCharity(viewModelScope: CoroutineScope, giftId: Long, rejectReason: String): LiveData<CustomResult<Any?>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                charityApi.rejectCharity(giftId, RejectGiftRequestModel(rejectReason))
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

    fun acceptCharity(viewModelScope: CoroutineScope, giftId: Long): LiveData<CustomResult<Any?>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                charityApi.acceptCharity(giftId)
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