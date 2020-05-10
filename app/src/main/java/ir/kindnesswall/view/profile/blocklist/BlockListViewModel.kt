package ir.kindnesswall.view.profile.blocklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.repository.UserRepo
import javax.inject.Inject

class BlockListViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    fun getBlockList() : LiveData<CustomResult<User>> {
        return userRepo.getUserProfile(viewModelScope)
    }
}