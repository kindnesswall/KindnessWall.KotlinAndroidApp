package ir.kindnesswall.data.api.di

import ir.kindnesswall.data.repositories.auth.AuthRemoteDataSourceImpl
import ir.kindnesswall.data.repositories.charity.CharityRemoteDataSourceImpl
import ir.kindnesswall.data.repositories.chat.ChatRemoteDataSourceImpl
import ir.kindnesswall.data.repositories.fileupload.FileUploadDataSourceImpl
import ir.kindnesswall.data.repositories.general.GeneralDataSourceImpl
import ir.kindnesswall.data.repositories.gift.GiftDataSourceImpl
import ir.kindnesswall.data.repositories.user.UserDataSourceImpl
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
    single { GeneralDataSourceImpl(generalApi = get(), appDatabase = get()) }
    single { CharityRemoteDataSourceImpl(charityApi = get()) }
    single { UserDataSourceImpl(userApi = get()) }
    single { ChatRemoteDataSourceImpl( chatApi = get()) }
    single { GiftDataSourceImpl(giftApi = get()) }
    single { FileUploadDataSourceImpl(userApi = get()) }

//    single { LocationHandler(get()) }
}