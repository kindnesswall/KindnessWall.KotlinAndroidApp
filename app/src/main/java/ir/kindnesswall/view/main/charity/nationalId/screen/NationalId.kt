package ir.kindnesswall.view.main.charity.nationalId.screen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.material.composethemeadapter.MdcTheme
import ir.kindnesswall.R
import ir.kindnesswall.view.main.charity.nationalId.NationalIdViewModel


@ExperimentalComposeUiApi
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun ScreenNationalId(viewModel: NationalIdViewModel){
    MdcTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(R.string.add_wallet)) })
            }
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (nextBtn, txtInformationNationalId, EditNationalId) = createRefs()
                var valueOfNationalId by remember { mutableStateOf("") }
                SlideOutVertically(addComponent = {
                    Text(
                        stringResource(id = R.string.detailsOFNationalId),
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }, txtInformationNationalId,viewModel.animationStateTextNationalId.value )
                SlideInHorizontally(
                    addComponent = {
                        NationalIdFiled(
                            query = valueOfNationalId,
                            onQueryChange = {
                                viewModel.valueOfNationalId.value = it
                            },
                            onSearchFocusChange = { },
                            onClearQuery = {
                                viewModel.valueOfNationalId.value  = ""
                            },
                            enableClose = viewModel.valueOfNationalId.value != "",
                        )


                    },
                    EditNationalId, txtInformationNationalId, viewModel.animationStateEditTextNationalId.value
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .constrainAs(nextBtn) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                        }, onClick = {

                    }) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }


    }
}