package com.farshidabz.kindnesswall.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.data.model.BaseModel


/**
 * Created by farshid.abazari since 2019-10-28
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class ResponseLiveData<T : BaseModel> : MutableLiveData<T>() {
    fun observeWithMessage(
        activity: AppCompatActivity?,
        owner: LifecycleOwner,
        observer: Observer<in T>
    ) {
        super.observe(owner, getObserver(activity, observer))
    }

    fun observeWithMessage(
        activity: FragmentActivity?,
        owner: LifecycleOwner,
        observer: Observer<in T>
    ) {
        super.observe(owner, getObserver(activity, observer))
    }

    fun observeWithMessage(
        activity: AppCompatActivity,
        observer: Observer<in T>
    ) {
        super.observe(activity, getObserver(activity, observer))
    }

    fun observeWithMessage(
        activity: FragmentActivity,
        observer: Observer<in T>
    ) {
        super.observe(activity, getObserver(activity, observer))
    }


    private fun getObserver(
        activity: AppCompatActivity?,
        observer: Observer<in T>
    ): Observer<in T> {
        return Observer {
            value?.let {
                if (activity is BaseActivity) {
                    activity.showToastMessage(it.apiResponseMessage)
                }
            }

            observer.onChanged(this.value)
        }
    }

    private fun getObserver(
        activity: FragmentActivity?,
        observer: Observer<in T>
    ): Observer<in T> {
        return Observer {
            value?.let {
                if (activity is BaseActivity) {
                    activity.showToastMessage(it.apiResponseMessage)
                }
            }

            observer.onChanged(this.value)
        }
    }
}
