package ir.kindnesswall.data.repositories.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.api.AuthApi
import ir.kindnesswall.domain.entities.LoginResponseModel
import ir.kindnesswall.domain.entities.RegisterBaseModel
import ir.kindnesswall.data.repositories.base.BaseDataSource
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.LoginUserRequestBodyBaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect

class AuthRemoteDataSourceImpl(
  private val authApi: AuthApi
) : AuthRemoteDataSource, BaseDataSource() {

  override fun registerUser(viewModelScope: CoroutineScope, phoneNumber: String): LiveData<CustomResult<Any?>> =
    liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
      emit(CustomResult.loading())
      getNullableResultWithExponentialBackoffStrategy {
        authApi.registerUser(RegisterBaseModel(phoneNumber))
      }.collect { result: CustomResult<Any?> ->
        when (result.status) {
          CustomResult.Status.SUCCESS -> {
            emit(CustomResult.success(result.data))
          }
          CustomResult.Status.ERROR -> {
            emit(CustomResult.error<Any>(result.errorMessage))
          }
          CustomResult.Status.LOADING -> emit(CustomResult.loading<Any>())
        }
      }
    }

  override fun loginUser(
    viewModelScope: CoroutineScope,
    phoneNumber: String,
    verificationCode: String
  ): LiveData<CustomResult<LoginResponseModel>> =
    liveData<CustomResult<LoginResponseModel>>(
      viewModelScope.coroutineContext,
      timeoutInMs = 0
    ) {
      emit(CustomResult.loading())

      getResultWithExponentialBackoffStrategy {
        authApi.loginUser(LoginUserRequestBodyBaseModel(phoneNumber, verificationCode))
      }.collect { result ->
        when (result.status) {
          CustomResult.Status.SUCCESS -> {
            emitSource(MutableLiveData<LoginResponseModel>().apply {
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
}