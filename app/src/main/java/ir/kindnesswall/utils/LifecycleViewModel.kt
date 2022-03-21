package ir.kindnesswall.utils

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel

/**
 * Created by Farhad Beigirad on 3/20/22.
 *
 *  This is a helper class as a workaround to avoid passing view related lifecycles to lower layers
 */
open class LifecycleViewModel : ViewModel(), LifecycleOwner {
    @SuppressLint("StaticFieldLeak")
    private var lifecycleRegistry: LifecycleRegistry? = null

    private fun initial() {
        if (lifecycleRegistry == null)
            lifecycleRegistry = LifecycleRegistry(this).apply { currentState = Lifecycle.State.STARTED }
    }

    override fun onCleared() {
        lifecycleRegistry?.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onCleared()
    }

    override fun getLifecycle(): Lifecycle {
        initial()
        return requireNotNull(lifecycleRegistry)
    }
}