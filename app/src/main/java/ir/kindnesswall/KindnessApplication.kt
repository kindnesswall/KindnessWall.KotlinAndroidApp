package ir.kindnesswall

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.chibatching.kotpref.Kotpref
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.di.dataBaseModule
import ir.kindnesswall.di.networkModule
import ir.kindnesswall.di.repositoryModule
import ir.kindnesswall.di.viewModelModule
import net.gotev.uploadservice.UploadServiceConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.Locale

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

class KindnessApplication : Application(), LifecycleObserver {
    companion object {
        const val uploadFileNotificationChannelID = "uploadChannel"
        lateinit var instance: KindnessApplication
    }

    init {
        instance = this
    }

    private var contactListMap = mutableMapOf<Long, ChatContactModel>()

    var deletedGifts = mutableListOf<GiftModel>()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        Kotpref.init(this)

        startKoin {
            androidLogger()
            androidContext(this@KindnessApplication)
            modules(listOf(repositoryModule, networkModule, viewModelModule, dataBaseModule))
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        createNotificationChannel()

        UploadServiceConfig.initialize(
            context = this,
            defaultNotificationChannel = uploadFileNotificationChannelID,
            debug = BuildConfig.DEBUG
        )

        changeLocale()
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

    fun setContactList(contactList: List<ChatContactModel>) {
        contactListMap.clear()
        contactListMap = contactList.map { it.chat?.chatId!! to it }.toMap().toMutableMap()
    }

    fun addOrUpdateContactList(chatContactModel: ChatContactModel) {
        contactListMap[chatContactModel.chat?.chatId!!] = chatContactModel
    }

    fun removeContact(chatId: Long){
        contactListMap.remove(chatId)
    }

    fun getContact(chatId: Long) = contactListMap[chatId]

    fun getContactList(): List<ChatContactModel> {
        return contactListMap.values.toList()
    }

    fun clearContactList() {
        contactListMap.clear()
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

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        AppPref.isAppInForeground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        AppPref.isAppInForeground = true
    }
}