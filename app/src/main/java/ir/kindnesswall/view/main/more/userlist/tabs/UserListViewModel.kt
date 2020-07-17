package ir.kindnesswall.view.main.more.userlist.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.requestsmodel.GetUsersRequestModel
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.repository.UserRepo
import ir.kindnesswall.view.main.more.userlist.adapter.GlobalFragmentPagerAdapter
import javax.inject.Inject


class UserListViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {

    val mUserList = ArrayList<User>()

    fun getUsers(userType: Int): LiveData<CustomResult<List<User>>>? {
        return when (userType) {
            GlobalFragmentPagerAdapter.TAB_USER_LIST_ACTIVE -> userRepo.getActiveUsers(
                viewModelScope,
                GetUsersRequestModel().apply { beforeId = getLastId() }
            )
            GlobalFragmentPagerAdapter.TAB_USER_LIST_BLOCK -> userRepo.getBlockedUsers(
                viewModelScope,
                GetUsersRequestModel().apply { beforeId = getLastId() }
            )
//            GlobalFragmentPagerAdapter.TAB_USER_LIST_CHAT_BLOCK ->   userRepo.getChatBlockedUsers(viewModelScope)
            else -> null
        }
    }

    private fun getLastId() = if (mUserList.isNotEmpty())
        mUserList.last().id;
    else null


}