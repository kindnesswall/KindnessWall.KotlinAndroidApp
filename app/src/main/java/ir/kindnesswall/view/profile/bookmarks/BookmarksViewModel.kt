package ir.kindnesswall.view.profile.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repositories.user.UserDataSource

class BookmarksViewModel(private val userRepo: UserDataSource) : ViewModel() {
//    val bookmarkList: LiveData<CustomResult<List<GiftModel>>>? by lazy {
//        userRepo.getBookmarkList(viewModelScope)
//    }
}