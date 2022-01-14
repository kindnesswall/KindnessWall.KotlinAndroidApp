package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.GiftRequestStatusModel
import ir.kindnesswall.data.model.PhoneNumberModel
import ir.kindnesswall.data.model.PhoneVisibility
import ir.kindnesswall.data.model.SetSetting
import ir.kindnesswall.data.model.SettingModel
import ir.kindnesswall.data.model.requestsmodel.DonateGiftRequestModel
import ir.kindnesswall.data.model.requestsmodel.GetGiftsRequestBaseBody
import ir.kindnesswall.data.model.requestsmodel.RejectGiftRequestModel
import ir.kindnesswall.data.remote.network.GiftApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

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
        liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                giftApi.getGifts(GetGiftsRequestBaseBody().apply { beforeId = lastId })
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

    fun searchForGifts(
        viewModelScope: CoroutineScope,
        lastId: Long,
        getGiftsRequestBody: GetGiftsRequestBaseBody
    ): LiveData<CustomResult<List<GiftModel>>> =
        liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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
        liveData<CustomResult<GiftModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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
        liveData<CustomResult<GiftModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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
        liveData<CustomResult<ChatContactModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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
        liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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
        liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getNullableResultWithExponentialBackoffStrategy {
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
        liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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
        liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                giftApi.getReviewGifts(GetGiftsRequestBaseBody().apply { beforeId = lastId })
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

    fun rejectGift(
        viewModelScope: CoroutineScope,
        giftId: Long,
        rejectReason: String
    ): LiveData<CustomResult<Any?>> =
        liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getNullableResultWithExponentialBackoffStrategy {
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
        liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getNullableResultWithExponentialBackoffStrategy {
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
        liveData<CustomResult<GiftRequestStatusModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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
        liveData<CustomResult<SettingModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
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
        liveData<CustomResult<PhoneNumberModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { giftApi.getUserNumber(userId) }.collect() { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource((MutableLiveData<PhoneNumberModel>().apply {
                            value = result.data as PhoneNumberModel?
                        }.map { CustomResult.success(it) }))
                    }
                    CustomResult.Status.ERROR ->{
                        emit(CustomResult.error(result.errorMessage))

                    }
                }

            }
        }

    fun deleteGift(
        viewModelScope: CoroutineScope,
        giftId: Long
    ): LiveData<CustomResult<Any?>> =
        liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getNullableResultWithExponentialBackoffStrategy { giftApi.deleteGift(giftId) }.collect { result ->
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

    fun setSettingNumber(
        context: CoroutineContext,
        visibility: PhoneVisibility
    ): LiveData<CustomResult<Any?>> = liveData<CustomResult<Any?>>(context, timeoutInMs = 0) {
        emit(CustomResult.loading())
        getNullableResultWithExponentialBackoffStrategy {
            val requestBody = SetSetting(
                when (visibility) {
                    PhoneVisibility.None -> "none"
                    PhoneVisibility.JustCharities -> "charity"
                    PhoneVisibility.All -> "all"
                }
            )
            giftApi.setPhoneVisibilitySetting(requestBody)
        }.collect { result ->
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

    fun getSettingNumber(context: CoroutineContext): LiveData<CustomResult<PhoneVisibility>> =
        liveData<CustomResult<PhoneVisibility>>(context, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getNullableResultWithExponentialBackoffStrategy { giftApi.getPhoneVisibilitySetting() }
                .collect { result: CustomResult<SettingModel?> ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS ->
                            emit(
                                CustomResult.success(
                                    when (result.data?.setting) {
                                        "charity" -> PhoneVisibility.JustCharities
                                        "all" -> PhoneVisibility.All
                                        "none",
                                        null -> PhoneVisibility.None // based on legacy codes null value is equal to none
                                        else -> error("Unknown value received for phone-visibility")
                                    }
                                )
                            )
                        CustomResult.Status.ERROR ->
                            emit(CustomResult.error(result.errorMessage, serverError = true))
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    }
                }
        }
}