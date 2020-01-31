package com.farshidabz.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.farshidabz.kindnesswall.data.model.BaseModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.remote.network.AuthApi

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: Authentication repository class to handle auth tasks
 *
 * How to call: just create a single object in AppInjector and pass it to viewModel
 *
 */

class AuthRepo(private val context: Context, private var authApi: AuthApi) {

    fun registerUser(phoneNumber: String): LiveData<CustomResult<BaseModel>>{
        val data = MutableLiveData<CustomResult<BaseModel>>()
//        authApi.registerUser(RegisterModel().apply { this.phoneNumber = phoneNumber })
//            .enqueue(object : RetrofitCallback<BaseModel> {
//                override fun success(response: Response<BaseModel>) {
//                    data.value = response.body()
//                }
//
//                override fun failure(
//                    response: Response<*>?,
//                    e: IOException?,
//                    t: Throwable?,
//                    networkResponseType: Int
//                ) {
//                    data.value = BaseModel().apply {
//                        isFailed = true
//                        statusCode = networkResponseType
//                        apiResponseMessage = buildAMessage(networkResponseType, response)
//                    }
//                }
//            })

        return data
    }
}