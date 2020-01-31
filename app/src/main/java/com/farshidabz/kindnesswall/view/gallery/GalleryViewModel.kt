package com.farshidabz.kindnesswall.view.gallery

import androidx.lifecycle.ViewModel

/**
 * Created by farshid.abazari since 3/26/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class GalleryViewModel : ViewModel() {
    lateinit var onGalleryButtonClickListener: OnGalleryButtonClickListener

    var images = ArrayList<String>()
    var currentImageIndex = 0

    fun onCloseClicked() {
        onGalleryButtonClickListener.onCloseClicked()
    }
}

interface OnGalleryButtonClickListener {
    fun onCloseClicked()
}