package com.farshidabz.kindnesswall.di

import com.farshidabz.kindnesswall.view.authentication.AuthenticationViewModel
import com.farshidabz.kindnesswall.view.catalog.cataloglist.CatalogViewModel
import com.farshidabz.kindnesswall.view.catalog.search.SearchViewModel
import com.farshidabz.kindnesswall.view.category.CategoryViewModel
import com.farshidabz.kindnesswall.view.charity.CharityListViewModel
import com.farshidabz.kindnesswall.view.charity.charitydetail.CharityViewModel
import com.farshidabz.kindnesswall.view.citychooser.CityChooserViewModel
import com.farshidabz.kindnesswall.view.gallery.GalleryViewModel
import com.farshidabz.kindnesswall.view.giftdetail.GiftDetailViewModel
import com.farshidabz.kindnesswall.view.main.MainViewModel
import com.farshidabz.kindnesswall.view.profile.MyProfileViewModel
import com.farshidabz.kindnesswall.view.profile.blocklist.BlockListViewModel
import com.farshidabz.kindnesswall.view.profile.bookmarks.BookmarksViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

val viewModelModule = module {
    viewModel { AuthenticationViewModel(get(), get()) }
    viewModel { MainViewModel() }
    viewModel { CatalogViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { CityChooserViewModel(get()) }
    viewModel { GiftDetailViewModel() }
    viewModel { GalleryViewModel() }
    viewModel { CharityListViewModel(get()) }
    viewModel { BookmarksViewModel(get()) }
    viewModel { BlockListViewModel() }
    viewModel { CharityViewModel(get()) }
    viewModel { MyProfileViewModel(get(), get()) }
}
