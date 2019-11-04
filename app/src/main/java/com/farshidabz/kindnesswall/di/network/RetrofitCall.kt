package com.farshidabz.kindnesswall.di.network


/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * Useful parameter:
 *
 */

interface RetrofitCall<T> {
    fun cancel()
    fun enqueue(callback: RetrofitCallback<T>)
}