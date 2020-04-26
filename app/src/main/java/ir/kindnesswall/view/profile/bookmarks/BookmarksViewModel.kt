package ir.kindnesswall.view.profile.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.UserRepo

class BookmarksViewModel(private val userRepo: UserRepo) : ViewModel() {
    val bookmarkList: LiveData<CustomResult<List<GiftModel>>>? by lazy {
        userRepo.getBookmarkList(viewModelScope)
    }
}