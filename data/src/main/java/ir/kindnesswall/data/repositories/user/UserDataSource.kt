package ir.kindnesswall.data.repositories.user

import androidx.lifecycle.LiveData
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.User
import kotlinx.coroutines.CoroutineScope

interface UserDataSource {

  fun getUserProfile(viewModelScope: CoroutineScope):
    LiveData<CustomResult<User>>

   fun getUserProfile(viewModelScope: CoroutineScope, userId: Long):
    LiveData<CustomResult<User>>

   fun updateUserProfile(
    viewModelScope: CoroutineScope,
    userName: String,
    imageUrl: String
  ): LiveData<CustomResult<Any>>

   fun getOtherUsersProfile(viewModelScope: CoroutineScope, userId: Long?):
    LiveData<CustomResult<User>>

   fun getUserReceivedGifts(viewModelScope: CoroutineScope, userId: Long?):
    LiveData<CustomResult<List<GiftModel>>>

   fun getUserDonatedGifts(viewModelScope: CoroutineScope, userId: Long?):
    LiveData<CustomResult<List<GiftModel>>>

   fun getUserRegisteredGifts(viewModelScope: CoroutineScope, userId: Long?):
    LiveData<CustomResult<List<GiftModel>>>

   fun getBookmarkList(viewModelScope: CoroutineScope): LiveData<CustomResult<List<GiftModel>>>?

   fun registerFirebaseToken()

   fun getUserAcceptedGifts(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

   fun getUserRejectedGifts(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

   fun setUserPhoneSetting(
    viewModelScope: CoroutineScope,
    setting: String
  ): LiveData<CustomResult<Any>>


}