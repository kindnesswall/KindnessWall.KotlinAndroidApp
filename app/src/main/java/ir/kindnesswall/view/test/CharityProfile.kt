package ir.kindnesswall.view.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.google.android.material.composethemeadapter.MdcTheme
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.view.main.charity.charitydetail.CharityDetailActivity
import ir.kindnesswall.view.uidesign.AppTextRow
import kotlinx.coroutines.launch
import timber.log.Timber

class CharityProfile : BaseFragment() {
    var name: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view as ComposeView
        view.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        view.setContent {
            MdcTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = stringResource(R.string.more_items)) })
                    }
                ) {
                    AppColumn()
                }
            }
        }
    }

    override fun configureViews() {
        //
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun AppColumn() {
    val tabs = listOf(TabItem.Booked, TabItem.Received)
    val pagerState = rememberPagerState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        AppTextRow(label = "نام خیریه :", initValue = "موسسه خیریه امام حسین", onTextChanged = {
            Timber.i(it)
        })
        Spacer(modifier = Modifier.height(8.dp))
        AppTextRow(label = "مدیر موسسه :", initValue = "علی عشقی", onTextChanged = {
            Timber.i(it)
        })
        Spacer(modifier = Modifier.height(8.dp))
        AppTextRow(label = "شماره تماس :", initValue = "09158537461", onTextChanged = {
            Timber.i(it)
        })
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            var charityModel = CharityModel(
                id = 1,
                userId = 0,
                name = "String? = null",
                isRejected = true,
                updatedAt = null,
                createdAt = null,
                imageUrl = "",
                registerId = "",
                address = "",
                telephoneNumber = "",
                mobileNumber = "",
                website = "",
                email = "",
                instagram = "",
                telegram = "",
                description = ""
            )
            CharityDetailActivity.start(context, charityModel)
        }) {
            Text(text = "صفحه خیریه")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.Black, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "لیست هدایا:")
        Tabs(tabs = tabs, pagerState = pagerState)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colorResource(id = R.color.colorPrimaryDark),
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            LeadingIconTab(
                icon = { },
                text = { Text(tab.title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen()
    }
}

@Preview
@Composable
fun AppColumnPreview() {
    MdcTheme { AppColumn() }
}