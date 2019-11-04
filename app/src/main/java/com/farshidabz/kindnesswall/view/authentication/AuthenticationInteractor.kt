package com.farshidabz.kindnesswall.view.authentication

import android.view.View


/**
 * Created by farshid.abazari since 2019-10-31
 *
 * Usage:
 *
 * Useful parameter:
 *
 */

interface AuthenticationInteractor {
    fun onPhoneNumberSent(view: View)
    fun onVerificationSent(view: View)
    fun onAuthenticationComplete(view: View)
}