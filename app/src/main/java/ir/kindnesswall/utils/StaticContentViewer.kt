package ir.kindnesswall.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

/**
 * Created by farshid.abazari since 2019-05-08
 *
 * Usage: A class to show static contents as webview
 *
 * How to call: just call @show
 *
 * Useful parameter: context to create object of customTab, staticContentType to show web page
 *
 */

object StaticContentViewer {
    fun show(context: Context, address: String?) {
        if (address.isNullOrEmpty()) {
            return
        }

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(address))
    }
}