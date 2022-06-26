package ir.kindnesswall.data.api.di

import ir.kindnesswall.data.api.*
import ir.kindnesswall.data.db.dao.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
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


val dataBaseModule = module {
    single { AppDatabase(androidContext()) }
}

const val baseNetworkQualifier = "baseNetwork"
const val authNetworkQualifier = "authNetwork"

val networkModule = module {
    single(named(baseNetworkQualifier)) {
        createBaseNetworkClient(androidContext())
    }
    single(named(authNetworkQualifier)) {
        createAuthNetworkClient(androidContext())
    }
    single {
        get<Retrofit>(named(authNetworkQualifier)).create(AuthApi::class.java)
    }
    single {
        get<Retrofit>(named(baseNetworkQualifier)).create(GeneralApi::class.java)
    }
    single {
        get<Retrofit>(named(baseNetworkQualifier)).create(ChatApi::class.java)
    }
    single {
        get<Retrofit>(named(baseNetworkQualifier)).create(UserApi::class.java)
    }
    single {
        get<Retrofit>(named(baseNetworkQualifier)).create(CharityApi::class.java)
    }
    single {
        get<Retrofit>(named(baseNetworkQualifier)).create(GiftApi::class.java)
    }
}

