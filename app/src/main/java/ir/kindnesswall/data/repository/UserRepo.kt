package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.requestsmodel.GetUsersRequestModel
import ir.kindnesswall.data.model.requestsmodel.PushRegisterRequestModel
import ir.kindnesswall.data.model.requestsmodel.UpdateProfileRequestBaseModel
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.remote.network.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                    userApi.getUserProfile(
                            UserInfoPref.userId
                    )
                }.collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            UserInfoPref.setUser(result.data)

                            emitSource(MutableLiveData<User>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                        CustomResult.Status.ERROR -> {
                            emit(CustomResult.error(result.errorMessage))
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    }
                }
            }

    fun getUserProfile(viewModelScope: CoroutineScope, userId: Long):
            LiveData<CustomResult<User>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
                emit(CustomResult.loading())

                getResultWithExponentialBackoffStrategy { userApi.getUserProfile(userId) }.collect { result ->
                    when (result.status) {
                        CustomResult.Status.SUCCESS -> {
                            emitSource(MutableLiveData<User>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                        CustomResult.Status.ERROR -> {
                            emit(CustomResult.error(result.errorMessage))
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
                            emit(CustomResult.error(result.errorMessage))
                        }
                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    }
                }
            }

    fun getOtherUsersProfile(viewModelScope: CoroutineScope, userId: Long?):
            LiveData<CustomResult<User>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
                if (userId == null) {
                    emit(CustomResult.error(CustomResult.ErrorMessage()))
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
                            emit(CustomResult.error(result.errorMessage))
                        }

                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    }
                }
            }

    fun getUserReceivedGifts(viewModelScope: CoroutineScope, userId: Long?):
            LiveData<CustomResult<List<GiftModel>>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
                if (userId == null) {
                    emit(CustomResult.error(CustomResult.ErrorMessage()))
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
                            emit(CustomResult.error(result.errorMessage))
                        }

                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    }
                }
            }

    fun getUserDonatedGifts(viewModelScope: CoroutineScope, userId: Long?):
            LiveData<CustomResult<List<GiftModel>>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
                if (userId == null) {
                    emit(CustomResult.error(CustomResult.ErrorMessage()))
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
                            emit(CustomResult.error(result.errorMessage))
                        }

                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    }
                }
            }

    fun getUserRegisteredGifts(viewModelScope: CoroutineScope, userId: Long?):
            LiveData<CustomResult<List<GiftModel>>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
                if (userId == null) {
                    emit(CustomResult.error(CustomResult.ErrorMessage()))
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
                            emit(CustomResult.error(result.errorMessage))
                        }

                        CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    }
                }
            }

    fun getBookmarkList(viewModelScope: CoroutineScope): LiveData<CustomResult<List<GiftModel>>>? {
        return null
    }

    fun registerFirebaseToken() {
        userApi.registerFirebaseToken(PushRegisterRequestModel(UserInfoPref.fireBaseToken))
                .enqueue(object : Callback<Any> {
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        AppPref.shouldUpdatedFireBaseToken = true
                    }

                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        AppPref.shouldUpdatedFireBaseToken = false
                    }
                })
    }

    fun getUserAcceptedGifts(
            viewModelScope: CoroutineScope,
            userId: Long
    ): LiveData<CustomResult<List<GiftModel>>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
                if (userId == null) {
                    emit(CustomResult.error(CustomResult.ErrorMessage()))
                    return@liveData
                }

                emit(CustomResult.loading())

                getResultWithExponentialBackoffStrategy { userApi.getUserReceivedGifts(userId) }
                        .collect { result ->
                            when (result.status) {
                                CustomResult.Status.SUCCESS -> {
                                    emitSource(MutableLiveData<List<GiftModel>>().apply {
                                        value = result.data
                                    }.map { CustomResult.success(it) })
                                }

                                CustomResult.Status.ERROR -> {
                                    emit(CustomResult.error(result.errorMessage))
                                }

                                CustomResult.Status.LOADING -> emit(CustomResult.loading())
                            }
                        }
            }

    fun getUserRejectedGifts(
            viewModelScope: CoroutineScope,
            userId: Long
    ): LiveData<CustomResult<List<GiftModel>>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
                if (userId == null) {
                    emit(CustomResult.error(CustomResult.ErrorMessage()))
                    return@liveData
                }

                emit(CustomResult.loading())

                getResultWithExponentialBackoffStrategy { userApi.getUserReceivedGifts(userId) }
                        .collect { result ->
                            when (result.status) {
                                CustomResult.Status.SUCCESS -> {
                                    emitSource(MutableLiveData<List<GiftModel>>().apply {
                                        value = result.data
                                    }.map { CustomResult.success(it) })
                                }

                                CustomResult.Status.ERROR -> {
                                    emit(CustomResult.error(result.errorMessage))
                                }

                                CustomResult.Status.LOADING -> emit(CustomResult.loading())
                            }
                        }
            }


    fun getActiveUsers(viewModelScope: CoroutineScope, model: GetUsersRequestModel): LiveData<CustomResult<List<User>>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {

                emit(CustomResult.loading())

                getResultWithExponentialBackoffStrategy { userApi.getActiveUsers(model) }
                        .collect { result ->
                            when (result.status) {
                                CustomResult.Status.SUCCESS -> emitSource(MutableLiveData<List<User>>().apply {
                                    value = result.data
                                }.map { CustomResult.success(it) })

                                CustomResult.Status.ERROR -> emit(CustomResult.error(result.errorMessage))

                                CustomResult.Status.LOADING -> emit(CustomResult.loading())
                            }
                        }
            }

    fun getBlockedUsers(viewModelScope: CoroutineScope,model: GetUsersRequestModel): LiveData<CustomResult<List<User>>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {

                emit(CustomResult.loading())

                getResultWithExponentialBackoffStrategy { userApi.getBlockedUsers(model) }
                        .collect { result ->
                            when (result.status) {
                                CustomResult.Status.SUCCESS -> emitSource(MutableLiveData<List<User>>().apply {
                                    value = result.data
                                }.map { CustomResult.success(it) })

                                CustomResult.Status.ERROR -> emit(CustomResult.error(result.errorMessage))

                                CustomResult.Status.LOADING -> emit(CustomResult.loading())
                            }
                        }
            }

    fun getChatBlockedUsers(viewModelScope: CoroutineScope,model: GetUsersRequestModel): LiveData<CustomResult<List<User>>> =
            liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {

                emit(CustomResult.loading())

                getResultWithExponentialBackoffStrategy { userApi.getChatBlockedUsers(model) }
                        .collect { result ->
                            when (result.status) {
                                CustomResult.Status.SUCCESS -> emitSource(MutableLiveData<List<User>>().apply {
                                    value = result.data
                                }.map { CustomResult.success(it) })

                                CustomResult.Status.ERROR -> emit(CustomResult.error(result.errorMessage))

                                CustomResult.Status.LOADING -> emit(CustomResult.loading())
                            }
                        }
            }
}
