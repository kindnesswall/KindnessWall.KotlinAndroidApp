package ir.kindnesswall.view.giftdetail

import android.view.View

interface GiftViewListener {
    fun onBackButtonClicked()
    fun onEditButtonClicked()
    fun onRequestClicked()
    fun onShareClicked()
    fun onBookmarkClicked()
    fun onAcceptGiftClicked()
    fun onRejectGiftClicked()
    fun onDeleteButtonClicked()
    fun onCallButtonClick(view: View)
    fun onCallPageClick()

}