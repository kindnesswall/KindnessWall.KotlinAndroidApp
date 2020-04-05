package com.farshidabz.kindnesswall.di

import com.farshidabz.kindnesswall.data.repository.*
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
    single { AuthRepo(androidContext(), authApi = get()) }
    single { GeneralRepo(androidContext(), generalApi = get(), appDatabase = get()) }
    single { CharityRepo(androidContext(), charityApi = get(), appDatabase = get()) }
    single { UserRepo(androidContext(), userApi = get()) }
    single { ChatRepo(androidContext(), chatApi = get()) }
    single { CatalogRepo(androidContext(), catalogApi = get(), appDatabase = get()) }
    single { FileUploadRepo(androidContext(), uploadFileApi = get()) }

//    single { LocationHandler(get()) }
}