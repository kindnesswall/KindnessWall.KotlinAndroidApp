package ir.kindnesswall.view.main

import androidx.lifecycle.ViewModel
import ir.kindnesswall.data.repository.UserRepo
import javax.inject.Inject


/**
 * Created by farshid.abazari since 2019-11-04
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class MainViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    var defaultTab: Int = 0
}