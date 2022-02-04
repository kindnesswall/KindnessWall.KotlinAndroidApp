package ir.kindnesswall.view.main.charity.nationalId.screen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.material.composethemeadapter.MdcTheme
import ir.kindnesswall.R
import ir.kindnesswall.view.main.charity.nationalId.*


@ExperimentalComposeUiApi
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun ScreenNationalId(viewModel: NationalIdViewModel) {
    MdcTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(R.string.add_wallet)) })
            }
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (nextBtn, txtInformationNationalId, EditNationalId, textStateRequest, loader, errorfindNatinal, listNational) = createRefs()
                SlideOutVertically(addComponent = {
                    Text(
                        stringResource(id = R.string.detailsOFNationalId),
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }, txtInformationNationalId, viewModel.animationStateTextNationalId.value)
                SlideInHorizontally(
                    addComponent = {
                        NationalIdFiled(
                            query = viewModel.valueOfNationalId.value,
                            onQueryChange = {
                                viewModel.valueOfNationalId.value = it
                            },
                            onSearchFocusChange = { },
                            onClearQuery = {
                                viewModel.valueOfNationalId.value = ""
                            },
                            enableClose = viewModel.valueOfNationalId.value != "",
                            colorBorder = viewModel.colorRoundEditFiled.value
                        )


                    },
                    EditNationalId,
                    txtInformationNationalId,
                    viewModel.animationStateEditTextNationalId.value
                )
                TextStateRequest(
                    viewModel.valueOfMessageRequest.value,
                    id = textStateRequest,
                    idTarget = EditNationalId
                )

                ErrorSearchNationalId(
                    viewModel.showErrorMessage.value,
                    errorfindNatinal,
                    textStateRequest
                )

                Loader(
                    viewModel.visibilityLoading.value,
                    anim = R.raw.loading_request,
                    id = loader,
                    idTarget = textStateRequest
                )
                ShowListNationalId(
                    listNational,
                    nextBtn,
                    EditNationalId,
                    viewModel.visibilityListNationalId.value,
                    viewModel.fakeGenerator.value
                )


                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.colorButton.value),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .constrainAs(nextBtn) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                        }, onClick = {
                        viewModel.handleEvent(ResultOfSearch)
                    }) {
                    Text(
                        color = Color.White,
                        text = stringResource(id = viewModel.textButton.value)
                    )
                }
            }
        }


    }
}