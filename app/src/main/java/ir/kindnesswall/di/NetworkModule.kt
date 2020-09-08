package ir.kindnesswall.di

import com.readystatesoftware.chuck.ChuckInterceptor
import android.content.Context
import ir.kindnesswall.BuildConfig
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.utils.extentions.addDebugInterceptor
import ir.kindnesswall.utils.wrapInBearer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
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

private fun getLogInterceptor() = HttpLoggingInterceptor().apply {
    level =
        sLogLevel
}

fun createBaseNetworkClient(context: Context) =
    retrofitClient(
        baseUrl,
        okHttpClient(context, true)
    )

fun createAuthNetworkClient(context: Context) =
    retrofitClient(
        baseUrl,
        okHttpClient(context, false)
    )

private fun okHttpClient(context: Context, addAuthHeader: Boolean) = OkHttpClient.Builder()
    .addInterceptor(getLogInterceptor()).apply {
        setTimeOutToOkHttpClient(
            this
        )
    }
    .addInterceptor(ChuckInterceptor(KindnessApplication.instance.applicationContext))
    .addInterceptor(headersInterceptor(addAuthHeader))
    .addDebugInterceptor(context)
    .build()

private fun retrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(NullOnEmptyConverterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun headersInterceptor(addAuthHeader: Boolean) = Interceptor { chain ->
    chain.proceed(
        chain.request().newBuilder()
            .also {
                if (addAuthHeader) {
                    it.addHeader("Authorization", wrapInBearer(UserInfoPref.bearerToken))
                }
                it.addHeader("Content-Type", "application/json")
            }
            .build()
    )
}

private fun setTimeOutToOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder) =
    okHttpClientBuilder.apply {
        readTimeout(BuildConfig.READ_TIMEOUT_SECOND, TimeUnit.SECONDS)
        connectTimeout(BuildConfig.CONNECTION_TIMEOUT_SECOND, TimeUnit.SECONDS)
        writeTimeout(BuildConfig.READ_TIMEOUT_SECOND, TimeUnit.SECONDS)
    }

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation?>?,
        retrofit: Retrofit
    ): Converter<ResponseBody, Any?> {
        val delegate: Converter<ResponseBody, Any> =
            retrofit.nextResponseBodyConverter(this, type, annotations)
        return Converter { body ->
            if (body.contentLength() == 0L) null else delegate.convert(body)
        }
    }
}