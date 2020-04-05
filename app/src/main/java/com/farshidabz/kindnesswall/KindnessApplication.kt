package com.farshidabz.kindnesswall

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.os.ConfigurationCompat
import com.chibatching.kotpref.Kotpref
import com.farshidabz.kindnesswall.data.local.AppPref
import com.farshidabz.kindnesswall.di.dataBaseModule
import com.farshidabz.kindnesswall.di.networkModule
import com.farshidabz.kindnesswall.di.repositoryModule
import com.farshidabz.kindnesswall.di.viewModelModule
import net.gotev.uploadservice.UploadServiceConfig
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
    companion object {
        const val uploadFileNotificationChannelID = "uploadChannel"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                uploadFileNotificationChannelID,
                "upload notification Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()

        Kotpref.init(this)

        startKoin {
            androidLogger()
            androidContext(this@KindnessApplication)
            modules(listOf(repositoryModule, networkModule, viewModelModule, dataBaseModule))
        }
        createNotificationChannel()

        UploadServiceConfig.initialize(
            context = this,
            defaultNotificationChannel = uploadFileNotificationChannelID,
            debug = BuildConfig.DEBUG
        )
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