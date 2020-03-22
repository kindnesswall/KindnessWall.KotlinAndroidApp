package com.farshidabz.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.farshidabz.kindnesswall.data.local.UserInfoPref
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.BaseDataSource
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.requestsmodel.UpdateProfileRequestBaseModel
import com.farshidabz.kindnesswall.data.model.user.User
import com.farshidabz.kindnesswall.data.remote.network.UserApi
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

class UserRepo(val context: Context, private val userApi: UserApi) : BaseDataSource() {
    fun getUserProfile(viewModelScope: CoroutineScope):
            LiveData<CustomResult<User>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                userApi.getUserProfile()
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        UserInfoPref.setUser(result.data)

                        emitSource(MutableLiveData<User>().apply {
                            value = result.data
                        }.map { CustomResult.success(it) })
                    }
                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(""))
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }

    fun updateUserProfile(
        viewModelScope: CoroutineScope,
        userName: String,
        imageUrl: String
    ): LiveData<CustomResult<Any>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                userApi.updateUserProfile(UpdateProfileRequestBaseModel(userName, imageUrl))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        UserInfoPref.image = imageUrl
                        UserInfoPref.name = userName

                        emitSource(MutableLiveData<Any>().apply {
                            value = result.data
                        }.map { CustomResult.success(it) })
                    }
                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(""))
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }

    fun getOtherUsersProfile(viewModelScope: CoroutineScope, userId: Long?):
            LiveData<CustomResult<User>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            if (userId == null) {
                emit(CustomResult.error(""))
                return@liveData
            }

            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                userApi.getOtherUserProfile(userId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<User>().apply { value = result.data }
                            .map { CustomResult.success(it) })
                    }

                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(""))
                    }

                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }

    fun getUserReceivedGifts(viewModelScope: CoroutineScope, userId: Long?):
            LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            if (userId == null) {
                emit(CustomResult.error(""))
                return@liveData
            }

            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                userApi.getUserReceivedGifts(userId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<List<GiftModel>>().apply { value = result.data }
                            .map { CustomResult.success(it) })
                    }

                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(""))
                    }

                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }

    fun getUserDonatedGifts(viewModelScope: CoroutineScope, userId: Long?):
            LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            if (userId == null) {
                emit(CustomResult.error(""))
                return@liveData
            }

            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                userApi.getUserDonatedGifts(userId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<List<GiftModel>>().apply { value = result.data }
                            .map { CustomResult.success(it) })
                    }

                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(""))
                    }

                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }

    fun getUserRegisteredGifts(viewModelScope: CoroutineScope, userId: Long?):
            LiveData<CustomResult<List<GiftModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            if (userId == null) {
                emit(CustomResult.error(""))
                return@liveData
            }

            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                userApi.getUserRegisteredGifts(userId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<List<GiftModel>>().apply { value = result.data }
                            .map { CustomResult.success(it) })
                    }

                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(""))
                    }

                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }
}