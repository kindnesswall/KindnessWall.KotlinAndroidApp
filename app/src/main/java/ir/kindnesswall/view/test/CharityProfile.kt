package ir.kindnesswall.view.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
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
        Tabs(tabs = tabs, pagerState = pagerState)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    TabRow(
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        backgroundColor = colorResource(id = R.color.colorPrimaryDark),
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        // Add tabs for all of our pages
        tabs.forEachIndexed { index, tab ->
            // OR Tab()
            LeadingIconTab(
                icon = { Icon(painter = painterResource(id = tab.icon), contentDescription = "") },
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