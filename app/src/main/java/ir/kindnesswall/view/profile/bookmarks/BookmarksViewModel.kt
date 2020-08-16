package ir.kindnesswall.view.profile.bookmarks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.UserRepo

class BookmarksViewModel(private val userRepo: UserRepo,application: Application) : AndroidViewModel(application) {
    val bookmarkList: LiveData<CustomResult<List<GiftModel>>>? by lazy {
        userRepo.getBookmarkList(viewModelScope)
    }
}