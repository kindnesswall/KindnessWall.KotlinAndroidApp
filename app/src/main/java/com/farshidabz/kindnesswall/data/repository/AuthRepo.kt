package com.farshidabz.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.farshidabz.kindnesswall.data.model.BaseDataSource
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.LoginResponseModel
import com.farshidabz.kindnesswall.data.model.requestsmodel.LoginUserRequestBodyModel
import com.farshidabz.kindnesswall.data.model.requestsmodel.RegisterModel
import com.farshidabz.kindnesswall.data.remote.network.AuthApi
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
    ): LiveData<CustomResult<Any>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                authApi.registerUser(RegisterModel(phoneNumber))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<Any>().map { CustomResult.success(it) })
                    }
                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(""))
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
                authApi.loginUser(LoginUserRequestBodyModel(phoneNumber, verificationCode))
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