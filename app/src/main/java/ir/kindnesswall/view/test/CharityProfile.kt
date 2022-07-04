package ir.kindnesswall.view.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.MdcTheme
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.view.uidesign.AppTextRow
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

@Composable
fun AppColumn() {
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
    }
}

@Preview
@Composable
fun AppColumnPreview() {
    MdcTheme { AppColumn() }
}