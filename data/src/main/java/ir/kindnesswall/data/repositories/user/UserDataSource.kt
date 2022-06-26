package ir.kindnesswall.data.repositories.user

import androidx.lifecycle.LiveData
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.User
import kotlinx.coroutines.CoroutineScope

interface UserDataSource {

  suspend fun getUserProfile(viewModelScope: CoroutineScope):
    LiveData<CustomResult<User>>

  suspend fun getUserProfile(viewModelScope: CoroutineScope, userId: Long):
    LiveData<CustomResult<User>>

  suspend fun updateUserProfile(
    viewModelScope: CoroutineScope,
    userName: String,
    imageUrl: String
  ): LiveData<CustomResult<Any>>

  suspend fun getOtherUsersProfile(viewModelScope: CoroutineScope, userId: Long?):
    LiveData<CustomResult<User>>

  suspend fun getUserReceivedGifts(viewModelScope: CoroutineScope, userId: Long?):
    LiveData<CustomResult<List<GiftModel>>>

  suspend fun getUserDonatedGifts(viewModelScope: CoroutineScope, userId: Long?):
    LiveData<CustomResult<List<GiftModel>>>

  suspend fun getUserRegisteredGifts(viewModelScope: CoroutineScope, userId: Long?):
    LiveData<CustomResult<List<GiftModel>>>

  suspend fun getBookmarkList(viewModelScope: CoroutineScope): LiveData<CustomResult<List<GiftModel>>>?

  suspend fun registerFirebaseToken()

  suspend fun getUserAcceptedGifts(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

  suspend fun getUserRejectedGifts(
    viewModelScope: CoroutineScope,
    userId: Long
  ): LiveData<CustomResult<List<GiftModel>>>

  suspend fun setUserPhoneSetting(
    viewModelScope: CoroutineScope,
    setting: String
  ): LiveData<CustomResult<Any>>


}