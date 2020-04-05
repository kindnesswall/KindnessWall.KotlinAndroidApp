package com.farshidabz.kindnesswall.view.giftdetail

import androidx.lifecycle.ViewModel
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel

class GiftDetailViewModel : ViewModel() {

    var selectedImageIndex: Int = 0

    var giftModel: GiftModel? = null

    var giftViewListener: GiftViewListener? = null

    fun onBackButtonClicked() {
        giftViewListener?.onBackButtonClicked()
    }

    fun onRequestClicked() {
        giftViewListener?.onRequestClicked()
    }

    fun onShareClicked() {
        giftViewListener?.onShareClicked()
    }

    fun onBookmarkClicked() {
        giftViewListener?.onBookmarkClicked()
    }
}