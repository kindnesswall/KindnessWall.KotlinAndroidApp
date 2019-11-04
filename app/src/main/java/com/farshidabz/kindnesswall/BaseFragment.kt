package com.farshidabz.kindnesswall

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment


/**
 * Created by farshid.abazari since 2019-10-31
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

@SuppressLint("Registered")
abstract class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureViewModel()
        configureViews()
    }

    abstract fun configureViewModel()
    abstract fun configureViews()
}