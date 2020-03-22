package com.farshidabz.kindnesswall.view.profile.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.repository.UserRepo

class BookmarksViewModel(private val userRepo: UserRepo) : ViewModel() {
    val bookmarkList: LiveData<CustomResult<List<GiftModel>>>? by lazy {
        userRepo.getBookmarkList(viewModelScope)
    }
}