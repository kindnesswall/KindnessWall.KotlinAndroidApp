package com.farshidabz.kindnesswall

import android.app.Application
import androidx.core.os.ConfigurationCompat
import com.chibatching.kotpref.Kotpref
import com.farshidabz.kindnesswall.data.local.AppPref
import com.farshidabz.kindnesswall.di.dataBaseModule
import com.farshidabz.kindnesswall.di.networkModule
import com.farshidabz.kindnesswall.di.repositoryModule
import com.farshidabz.kindnesswall.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*


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

        Kotpref.init(this)

        startKoin {
            androidLogger()
            androidContext(this@KindnessApplication)
            modules(listOf(repositoryModule, networkModule, viewModelModule, dataBaseModule))
        }

        changeLocale()
    }

    private fun changeLocale() {
        var currentAppLocaleLanguage = AppPref.currentLocale

        if (currentAppLocaleLanguage.isEmpty()) {
            currentAppLocaleLanguage =
                ConfigurationCompat.getLocales(AppPref.context.resources.configuration)[0].language

            AppPref.currentLocale = currentAppLocaleLanguage
        }

        val locale = Locale(currentAppLocaleLanguage)

        Locale.setDefault(locale)
        val config = baseContext.resources.configuration
        config.locale = locale

        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }
}