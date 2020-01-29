package com.farshidabz.kindnesswall.di

import com.farshidabz.kindnesswall.data.local.dao.catalog.CatalogDatabase
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceDatabase
import com.farshidabz.kindnesswall.data.remote.network.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit


/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: this methods handle DI
 *
 * How to call: just startActivityForResult koin in application class
 *
 */

private val baseRetrofit: Retrofit = createBaseNetworkClient()
private val authRetrofit: Retrofit = createAuthNetworkClient()

private val authApi: AuthApi = authRetrofit.create(AuthApi::class.java)

private val generalApi: GeneralApi = baseRetrofit.create(GeneralApi::class.java)
private val chatApi: ChatApi = baseRetrofit.create(ChatApi::class.java)
private val userApi: UserApi = baseRetrofit.create(UserApi::class.java)
private val charityApi: CharityApi = baseRetrofit.create(CharityApi::class.java)
private val catalogApi: CatalogApi = baseRetrofit.create(CatalogApi::class.java)

val dataBaseModule = module {
    single { CatalogDatabase(androidContext()).catalogDao() }
    single { ProvinceDatabase(androidContext()).provinceDao() }
}

val networkModule = module {
    single { authApi }
    single { generalApi }
    single { chatApi }
    single { userApi }
    single { charityApi }
    single { catalogApi }
}

//val remoteConfigModule = module {
//    single { RemoteConfigRepo(androidContext(), getRemoteConfigInstance()) }
//}
