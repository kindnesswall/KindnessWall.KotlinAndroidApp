package ir.kindnesswall.data.repositories.gift

import androidx.lifecycle.LiveData
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.data.db.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface GiftDataSource {

  suspend fun getGifts(
    viewModelScope: CoroutineScope,
    lastId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

  suspend fun searchForGifts(
    viewModelScope: CoroutineScope,
    lastId: Long,
    getGiftsRequestBody: GetGiftsRequestBaseBody
  ): LiveData<CustomResult<List<GiftModel>>>

  suspend fun registerGift(
    viewModelScope: CoroutineScope,
    registerGiftRequestModel: RegisterGiftRequestModel
  ): LiveData<CustomResult<GiftModel>>

  suspend fun updateGift(
    viewModelScope: CoroutineScope,
    registerGiftRequestModel: RegisterGiftRequestModel
  ): LiveData<CustomResult<GiftModel>>

  suspend fun requestGift(viewModelScope: CoroutineScope, giftId: Long):
    LiveData<CustomResult<ChatContactModel>>

  suspend fun  getToDonateGifts(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

  suspend fun donateGift(
    viewModelScope: CoroutineScope,
    giftId: Long,
    userToDonateId: Long
  ): LiveData<CustomResult<Any?>>

  suspend fun getReviewGiftsFirstPage(
    viewModelScope: CoroutineScope
  ): LiveData<CustomResult<List<GiftModel>>>

  suspend fun getReviewGifts(
    viewModelScope: CoroutineScope,
    lastId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

  suspend fun rejectGift(
    viewModelScope: CoroutineScope,
    giftId: Long,
    rejectReason: String
  ): LiveData<CustomResult<Any?>>

  suspend fun acceptGift(
    viewModelScope: CoroutineScope,
    giftId: Long
  ): LiveData<CustomResult<Any?>>

  suspend fun getGiftRequestStatus(
    viewModelScope: CoroutineScope,
    giftId: Long
  ): LiveData<CustomResult<GiftRequestStatusModel>>

  suspend fun getSetting(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<SettingModel>>

 suspend fun getUserNmber(
   viewModelScope: CoroutineScope,
   userId: Long
 ): LiveData<CustomResult<PhoneNumberModel>>

 suspend fun deleteGift(
   viewModelScope: CoroutineScope,
   giftId: Long
 ): LiveData<CustomResult<Any?>>

 suspend fun setSettingNumber(
   context: CoroutineContext,
   visibility: PhoneVisibility
 ): LiveData<CustomResult<Any?>>

 suspend fun getSettingNumber(context: CoroutineContext): LiveData<CustomResult<PhoneVisibility>>
}