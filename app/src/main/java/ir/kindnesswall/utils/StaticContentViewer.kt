package ir.kindnesswall.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import com.google.android.material.snackbar.Snackbar
import ir.kindnesswall.R

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
        try {
            if (address.isNullOrEmpty()) {
                return
            }

            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(address))
        } catch (ex: Exception) {
            ex.printStackTrace()
            (context as? Activity)?.findViewById<View>(android.R.id.content)?.let {
                Snackbar.make(
                    it,
                    context.getString(R.string.notfound_related_app),
                    Snackbar.LENGTH_SHORT
                ).setTextColor(Color.WHITE).show()
            }
        }
    }
}