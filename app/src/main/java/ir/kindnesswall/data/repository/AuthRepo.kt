package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.LoginResponseModel
import ir.kindnesswall.data.model.requestsmodel.LoginUserRequestBodyBaseModel
import ir.kindnesswall.data.model.requestsmodel.RegisterBaseModel
import ir.kindnesswall.data.remote.network.AuthApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: Authentication repository class to handle auth tasks
 *
 * How to call: just create a single object in AppInjector and pass it to viewModel
 *
 */

class AuthRepo(private val context: Context, private var authApi: AuthApi) : BaseDataSource() {

    fun registerUser(
        viewModelScope: CoroutineScope,
        phoneNumber: String
    ): LiveData<CustomResult<Any?>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                authApi.registerUser(RegisterBaseModel(phoneNumber))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emit(CustomResult.success(result.data))
                    }
                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(result.message.toString()))
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }

    fun loginUser(viewModelScope: CoroutineScope, phoneNumber: String, verificationCode: String):
            LiveData<CustomResult<LoginResponseModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
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
                        emit(CustomResult.error(""))
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }
}