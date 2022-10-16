package ir.kindnesswall.data.repositories.gift

import androidx.lifecycle.LiveData
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.data.db.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface GiftDataSource {

  fun getGifts(
    viewModelScope: CoroutineScope,
    lastId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

  fun searchForGifts(
    viewModelScope: CoroutineScope,
    lastId: Long,
    getGiftsRequestBody: GetGiftsRequestBaseBody
  ): LiveData<CustomResult<List<GiftModel>>>

  fun registerGift(
    viewModelScope: CoroutineScope,
    registerGiftRequestModel: RegisterGiftRequestModel
  ): LiveData<CustomResult<GiftModel>>

  fun updateGift(
    viewModelScope: CoroutineScope,
    registerGiftRequestModel: RegisterGiftRequestModel
  ): LiveData<CustomResult<GiftModel>>

  fun requestGift(viewModelScope: CoroutineScope, giftId: Long):
    LiveData<CustomResult<ChatContactModel>>

  fun  getToDonateGifts(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

  fun donateGift(
    viewModelScope: CoroutineScope,
    giftId: Long,
    userToDonateId: Long
  ): LiveData<CustomResult<Any?>>

  fun getReviewGiftsFirstPage(
    viewModelScope: CoroutineScope
  ): LiveData<CustomResult<List<GiftModel>>>

  fun getReviewGifts(
    viewModelScope: CoroutineScope,
    lastId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

  fun rejectGift(
    viewModelScope: CoroutineScope,
    giftId: Long,
    rejectReason: String
  ): LiveData<CustomResult<Any?>>

  fun acceptGift(
    viewModelScope: CoroutineScope,
    giftId: Long
  ): LiveData<CustomResult<Any?>>

  fun getGiftRequestStatus(
    viewModelScope: CoroutineScope,
    giftId: Long
  ): LiveData<CustomResult<GiftRequestStatusModel>>

  fun getSetting(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<SettingModel>>

  fun getUserNmber(
   viewModelScope: CoroutineScope,
   userId: Long
 ): LiveData<CustomResult<PhoneNumberModel>>

  fun deleteGift(
   viewModelScope: CoroutineScope,
   giftId: Long
 ): LiveData<CustomResult<Any?>>

  fun setSettingNumber(
    context: CoroutineContext,
    visibility:
    PhoneVisibility
 ): LiveData<CustomResult<Any?>>

  fun getSettingNumber(context: CoroutineContext): LiveData<CustomResult<PhoneVisibility>>
}