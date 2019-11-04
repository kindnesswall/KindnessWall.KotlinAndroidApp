package com.farshidabz.kindnesswall

import android.app.Application
import com.farshidabz.kindnesswall.di.networkModule
import com.farshidabz.kindnesswall.di.repositoryModule
import com.farshidabz.kindnesswall.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


/**
 * Created by farshid.abazari since 2019-11-01
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class KindnessApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@KindnessApplication)
            modules(listOf(repositoryModule, networkModule, viewModelModule))
        }
    }
}