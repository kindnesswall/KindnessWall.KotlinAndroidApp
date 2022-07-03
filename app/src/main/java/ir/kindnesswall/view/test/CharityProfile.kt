package ir.kindnesswall.view.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.MdcTheme
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import timber.log.Timber

class CharityProfile : BaseFragment() {
    var name: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    Column() {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Name:",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.body1
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TextField(
                                value = name,
                                modifier = Modifier.weight(1f),
                                onValueChange = {
                                    Timber.i(it)
                                    name = it
                                })
                            Button(onClick = {

                                //api call
                            }) {
                                Text(text = "Click ME!")
                            }
                        }
                        Text("Hello Farhad!")
                        Text("Hello Hamed!")
                    }
                }
            }
        }
    }

    override fun configureViews() {

    }
}