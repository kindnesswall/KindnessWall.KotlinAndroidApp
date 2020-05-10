package ir.kindnesswall.di

import ir.kindnesswall.view.authentication.AuthenticationViewModel
import ir.kindnesswall.view.category.CategoryViewModel
import ir.kindnesswall.view.citychooser.CityChooserViewModel
import ir.kindnesswall.view.gallery.GalleryViewModel
import ir.kindnesswall.view.giftdetail.GiftDetailViewModel
import ir.kindnesswall.view.main.MainViewModel
import ir.kindnesswall.view.main.addproduct.SubmitGiftViewModel
import ir.kindnesswall.view.main.catalog.cataloglist.CatalogViewModel
import ir.kindnesswall.view.main.catalog.search.SearchViewModel
import ir.kindnesswall.view.main.charity.CharityListViewModel
import ir.kindnesswall.view.main.charity.charitydetail.CharityViewModel
import ir.kindnesswall.view.main.conversation.ConversationsViewModel
import ir.kindnesswall.view.main.conversation.chat.ChatViewModel
import ir.kindnesswall.view.profile.MyProfileViewModel
import ir.kindnesswall.view.profile.blocklist.BlockListViewModel
import ir.kindnesswall.view.profile.bookmarks.BookmarksViewModel
import ir.kindnesswall.view.splash.SplashViewModel
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
    viewModel { MainViewModel(get()) }
    viewModel { CatalogViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { CityChooserViewModel(get()) }
    viewModel { GiftDetailViewModel(get()) }
    viewModel { GalleryViewModel() }
    viewModel { CharityListViewModel(get()) }
    viewModel { BookmarksViewModel(get()) }
    viewModel { BlockListViewModel(get()) }
    viewModel { CharityViewModel(get(), get()) }
    viewModel { MyProfileViewModel(get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { SubmitGiftViewModel(get(), get(), get()) }
    viewModel { ConversationsViewModel(get()) }
    viewModel { ChatViewModel(get(), get()) }
}
