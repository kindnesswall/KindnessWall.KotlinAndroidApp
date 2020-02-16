package com.farshidabz.kindnesswall.di

import com.farshidabz.kindnesswall.view.authentication.AuthenticationViewModel
import com.farshidabz.kindnesswall.view.catalog.cataloglist.CatalogViewModel
import com.farshidabz.kindnesswall.view.catalog.search.SearchViewModel
import com.farshidabz.kindnesswall.view.charity.CharityListViewModel
import com.farshidabz.kindnesswall.view.citychooser.CityChooserViewModel
import com.farshidabz.kindnesswall.view.gallery.GalleryViewModel
import com.farshidabz.kindnesswall.view.giftdetail.GiftDetailViewModel
import com.farshidabz.kindnesswall.view.main.MainViewModel
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
    viewModel { CityChooserViewModel(get()) }
    viewModel { GiftDetailViewModel() }
    viewModel { GalleryViewModel() }
    viewModel { CharityListViewModel(get()) }
}
