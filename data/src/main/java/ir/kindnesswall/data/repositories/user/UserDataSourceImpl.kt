package ir.kindnesswall.data.repositories.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.api.UserApi
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.data.repositories.base.BaseDataSource
import ir.kindnesswall.domain.common.AppPref
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.PushRegisterRequestModel
import ir.kindnesswall.domain.entities.UpdateProfileRequestBaseModel
import ir.kindnesswall.domain.entities.User
import ir.kindnesswall.utils.UserInfoPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDataSourceImpl(
  private val userApi: UserApi
) : UserDataSource, BaseDataSource() {
  override suspend fun getUserProfile(viewModelScope: CoroutineScope): LiveData<CustomResult<User>> =
    liveData<CustomResult<User>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
      emit(CustomResult.loading())

      getResultWithExponentialBackoffStrategy {
        userApi.getUserProfile(
          UserInfoPref.userId
        )
      }.collect { result ->
        when (result.status) {
          CustomResult.Status.SUCCESS -> {
            UserInfoPref.setUser(result.data)
            Log.i("46546465564", result.data.toString())
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

  override suspend fun getUserProfile(viewModelScope: CoroutineScope, userId: Long): LiveData<CustomResult<User>> =
    liveData<CustomResult<User>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

  override suspend fun updateUserProfile(
    viewModelScope: CoroutineScope,
    userName: String,
    imageUrl: String
  ): LiveData<CustomResult<Any>> =
    liveData<CustomResult<Any>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
      emit(CustomResult.loading())
      getNullableResultWithExponentialBackoffStrategy {
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

  override suspend fun getOtherUsersProfile(
    viewModelScope: CoroutineScope,
    userId: Long?
  ): LiveData<CustomResult<User>> =
    liveData<CustomResult<User>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

  override suspend fun getUserReceivedGifts(
    viewModelScope: CoroutineScope,
    userId: Long?
  ): LiveData<CustomResult<List<GiftModel>>> =
    liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

  override suspend fun getUserDonatedGifts(
    viewModelScope: CoroutineScope,
    userId: Long?
  ): LiveData<CustomResult<List<GiftModel>>> =
    liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

  override suspend fun getUserRegisteredGifts(
    viewModelScope: CoroutineScope,
    userId: Long?
  ): LiveData<CustomResult<List<GiftModel>>> =
    liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

  override suspend fun getBookmarkList(viewModelScope: CoroutineScope): LiveData<CustomResult<List<GiftModel>>>? {
    return null
  }

  override suspend fun registerFirebaseToken() {
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

  override suspend fun getUserAcceptedGifts(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<List<GiftModel>>> =
    liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

  override suspend fun getUserRejectedGifts(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<List<GiftModel>>> =
    liveData<CustomResult<List<GiftModel>>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
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

  override suspend fun setUserPhoneSetting(
    viewModelScope: CoroutineScope,
    setting: String
  ): LiveData<CustomResult<Any>> =
    liveData<CustomResult<Any>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
      Log.i("56456465456", "Run2")
      emit(CustomResult.loading())
      getResultWithExponentialBackoffStrategy {
        userApi.setPhoneSetting(setting)
      }.collect { result ->
        Log.i("56456465456", "Run3")

        when (result.status) {
          CustomResult.Status.SUCCESS -> {
            Log.i("56456465456", "SUCCESS")
          }
          CustomResult.Status.ERROR -> {
            Log.i("56456465456", "ERROR")
          }
        }
      }
    }
}