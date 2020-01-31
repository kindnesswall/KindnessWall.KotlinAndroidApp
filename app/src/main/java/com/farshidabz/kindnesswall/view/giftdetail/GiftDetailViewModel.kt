package com.farshidabz.kindnesswall.view.giftdetail

import androidx.lifecycle.ViewModel
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel

class GiftDetailViewModel : ViewModel() {

    var selectedImageIndex: Int = 0

    var giftModel: GiftModel? = null

    public var giftViewListener: GiftViewListener? = null

    public fun onBackButtonClicked() {
        giftViewListener?.onBackButtonClicked()
    }

    public fun onRequestClicked() {
        giftViewListener?.onRequestClicked()
    }

    public fun onShareClicked() {
        giftViewListener?.onShareClicked()
    }

    public fun onBookmarkClicked() {
        giftViewListener?.onBookmarkClicked()
    }
}