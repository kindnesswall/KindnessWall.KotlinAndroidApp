package ir.kindnesswall.di

import ir.kindnesswall.data.repositories.auth.AuthRemoteDataSourceImpl
import ir.kindnesswall.data.repositories.gift.GiftDataSourceImpl
import ir.kindnesswall.data.repositories.user.UserDataSource
import ir.kindnesswall.data.repositories.user.UserDataSourceImpl
import ir.kindnesswall.data.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


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

val repositoryModule = module {
    single { AuthRemoteDataSourceImpl(authApi = get()) }
    single { GeneralRepo(androidContext(), generalApi = get(), appDatabase = get()) }
    single { CharityRepo(androidContext(), charityApi = get()) }
    single { UserDataSourceImpl(userApi = get()) }
    single { ChatRepo(androidContext(), chatApi = get()) }
    single { GiftDataSourceImpl(giftApi = get()) }
    single { FileUploadRepo(androidContext(), userApi = get()) }

//    single { LocationHandler(get()) }
}