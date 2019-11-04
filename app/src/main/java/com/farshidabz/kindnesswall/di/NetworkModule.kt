package com.farshidabz.kindnesswall.di

import com.farshidabz.kindnesswall.BuildConfig
import com.farshidabz.kindnesswall.di.network.ErrorHandlingCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: functions to create network requirements such as okHttpClient
 *
 * How to call: just call [createBaseNetworkClient] in AppInjector
 *
 */


private val sLogLevel =
    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

private const val baseUrl = BuildConfig.URL_WEBAPI
private const val fcmUrl = "https://fcm.googleapis.com"

private fun getLogInterceptor() = HttpLoggingInterceptor().apply {
    level =
        sLogLevel
}

fun createBaseNetworkClient() =
    retrofitClient(
        baseUrl,
        okHttpClient(true)
    )

fun createAuthNetworkClient() =
    retrofitClient(
        baseUrl,
        okHttpClient(false)
    )

private fun okHttpClient(addAuthHeader: Boolean) = OkHttpClient.Builder()
    .addInterceptor(getLogInterceptor()).apply {
        setTimeOutToOkHttpClient(
            this
        )
    }
    .addInterceptor(headersInterceptor(addAuthHeader)).build()

private fun retrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ErrorHandlingCallAdapterFactory())
        .build()

fun headersInterceptor(addAuthHeader: Boolean) = Interceptor { chain ->
    chain.proceed(
        chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
//            .also {
//                if (addAuthHeader) {
//                    it.addHeader("Authorization", wrapInBearer(UserInfoPref.bearerToken))
//                }
//            }
            .build()
    )
}

private fun setTimeOutToOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder) =
    okHttpClientBuilder.apply {
        readTimeout(BuildConfig.READ_TIMEOUT_SECOND, TimeUnit.SECONDS)
        connectTimeout(BuildConfig.CONNECTION_TIMEOUT_SECOND, TimeUnit.SECONDS)
        writeTimeout(BuildConfig.READ_TIMEOUT_SECOND, TimeUnit.SECONDS)
    }