package ir.kindnesswall.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.data.model.*
import ir.kindnesswall.data.model.requestsmodel.DonateGiftRequestModel
import ir.kindnesswall.data.model.requestsmodel.GetGiftsRequestBaseBody
import ir.kindnesswall.data.model.requestsmodel.RejectGiftRequestModel
import ir.kindnesswall.data.remote.network.GiftApi
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

class GiftRepo(context: Context, private val giftApi: GiftApi) : BaseDataSource(context) {
    fun getGifts(
        viewModelScope: CoroutineScope,
        lastId: Long
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                giftApi.getGifts(GetGiftsRequestBaseBody().apply { beforeId = lastId })
            }.collect { result ->
                Log.i("54646165464654564646", (result.data).toString())
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<List<GiftModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
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
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<List<GiftModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
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
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<GiftModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun updateGift(
        viewModelScope: CoroutineScope,
        registerGiftRequestModel: RegisterGiftRequestModel
    ): LiveData<CustomResult<GiftModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                giftApi.updateGift(registerGiftRequestModel.id.toLong(), registerGiftRequestModel)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<GiftModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun requestGift(viewModelScope: CoroutineScope, giftId: Long):
            LiveData<CustomResult<ChatContactModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { giftApi.requestGift(giftId) }
                .collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            if (result.data == null) {
                                emit(CustomResult.error(result.errorMessage))
                            } else {
                                emitSource(MutableLiveData<ChatContactModel>().apply {
                                    value = result.data
                                }.map { CustomResult.success(it) })
                            }
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                        else -> emit(CustomResult.error(result.errorMessage))
                    }
                }
        }

    fun getToDonateGifts(
        viewModelScope: CoroutineScope,
        userId: Long
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                giftApi.getToDonateGifts(userId, GetGiftsRequestBaseBody().apply {
                    beforeId = null
                    count = null
                    countryId = null
                })
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<List<GiftModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun donateGift(
        viewModelScope: CoroutineScope,
        giftId: Long,
        userToDonateId: Long
    ): LiveData<CustomResult<Any?>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                giftApi.donateGift(DonateGiftRequestModel(giftId, userToDonateId))
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

    fun getReviewGiftsFirstPage(
        viewModelScope: CoroutineScope
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                giftApi.getReviewGiftsFirstPage(GetGiftsRequestBaseBody())
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<List<GiftModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun getReviewGifts(
        viewModelScope: CoroutineScope,
        lastId: Long
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                giftApi.getReviewGifts(GetGiftsRequestBaseBody().apply { beforeId = lastId })
            }.collect { result ->
                Log.i("54646165464654564646", (result.data).toString())

                when (result.status) {

                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<List<GiftModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }

            }

        }

    fun rejectGift(
        viewModelScope: CoroutineScope,
        giftId: Long,
        rejectReason: String
    ): LiveData<CustomResult<Any?>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                giftApi.rejectGift(giftId, RejectGiftRequestModel(rejectReason))
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

    fun acceptGift(
        viewModelScope: CoroutineScope,
        giftId: Long
    ): LiveData<CustomResult<Any?>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                giftApi.acceptGift(giftId)
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

    fun getGiftRequestStatus(
        viewModelScope: CoroutineScope,
        giftId: Long
    ): LiveData<CustomResult<GiftRequestStatusModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                giftApi.getGiftRequestStatus(giftId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<GiftRequestStatusModel>().apply {
                            value = result.data
                        }.map { CustomResult.success(it) })
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> {
                        emit(CustomResult.error(result.errorMessage))
                    }
                }
            }
        }

    fun getSetting(
        viewModelScope: CoroutineScope,
        userId: Long
    ): LiveData<CustomResult<SettingModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            Log.i("545645656465", userId.toString())
            getResultWithExponentialBackoffStrategy { giftApi.getSetting(userId) }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<SettingModel>().apply {
                            value = result.data as SettingModel?
                        }.map { CustomResult.success(it) })
                    }
                }
            }
        }

    fun getUserNmber(
        viewModelScope: CoroutineScope,
        userId: Long
    ): LiveData<CustomResult<PhoneNumberModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { giftApi.getUserNumber(userId) }.collect() { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource((MutableLiveData<PhoneNumberModel>().apply {
                            value = result.data as PhoneNumberModel?
                        }.map { CustomResult.success(it) }))
                    }
                }

            }
        }

    fun deleteGift(
        viewModelScope: CoroutineScope,
        giftId: Long
    ): LiveData<CustomResult<Any?>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { giftApi.deleteGift(giftId) }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emit(CustomResult.success(result.data))
                    }
                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(result.errorMessage))
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }

    fun SetSettingNumber(
        viewModelScope: CoroutineScope,
        value: String
    ): LiveData<CustomResult<Any?>> = liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
        emit(CustomResult.loading())
        getResultWithExponentialBackoffStrategy { giftApi.setPhoneVisibilitySetting(SetSetting(value)) }.collect { result ->
            Log.i("4566456456465465",result.status.toString()+"SUCCESS")
            when (result.status) {
                CustomResult.Status.SUCCESS -> {
                Log.i("4566456456465465","SUCCESS")
                }
                CustomResult.Status.ERROR -> {
                    Log.i("4566456456465465","ERROR")
                }
                CustomResult.Status.LOADING -> emit(CustomResult.loading())
            }
        }

    }
    fun getSettingNumber(viewModelScope: CoroutineScope):LiveData<CustomResult<SettingModel?>> = liveData(viewModelScope.coroutineContext,timeoutInMs = 0){
        emit(CustomResult.loading())
        getResultWithExponentialBackoffStrategy{giftApi.getPhoneVisibilitySetting()}.collect{result ->
            when (result.status) {
                CustomResult.Status.SUCCESS -> {
                    emit(CustomResult.success(result.data))
                    Log.i("4566456456465465","SUCCESS   "+result.data!!.setting)

                }
                CustomResult.Status.ERROR -> {
                    Log.i("4566456456465465","ERROR")
                }
                CustomResult.Status.LOADING -> emit(CustomResult.loading())
            }
        }
    }
}