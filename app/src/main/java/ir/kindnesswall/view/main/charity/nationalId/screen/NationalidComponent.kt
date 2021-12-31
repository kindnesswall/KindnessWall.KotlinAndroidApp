package ir.kindnesswall.view.main.charity.nationalId.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import com.google.android.material.composethemeadapter.MdcTheme


@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ConstraintLayoutScope.SlideInHorizontally(
    addComponent: @Composable () -> Unit,
    id: ConstrainedLayoutReference,
    idDependency: ConstrainedLayoutReference,
    visible: Boolean
) {
    AnimatedVisibility(
        modifier = Modifier
            .constrainAs(id) {
                top.linkTo(idDependency.bottom, margin = 5.dp)
            }
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 15.dp),
        visible = visible,
        enter = slideInHorizontally(animationSpec = tween(durationMillis = 800)) { fullWidth ->
            -fullWidth / 3
        } + fadeIn(
            animationSpec = tween(durationMillis = 800)
        ),
        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
            200
        } + fadeOut()
    ) {
        addComponent()
    }
}

@ExperimentalAnimationApi
@Composable
fun ConstraintLayoutScope.SlideOutVertically(addComponent: @Composable () -> Unit, id: ConstrainedLayoutReference, visible: Boolean) {
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .constrainAs(id) {
                top.linkTo(parent.top)
            }
            .padding(top = 20.dp),
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + scaleIn(
            transformOrigin = TransformOrigin(0.5f, 0f)
        ) + fadeIn(initialAlpha = 0.8f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(
            targetScale = 1.2f
        )
    ) {
        addComponent()

    }
}


@Composable
fun mirroringIcon(ltrIcon: ImageVector, rtlIcon: ImageVector): ImageVector =
    if (LocalLayoutDirection.current == LayoutDirection.Ltr) ltrIcon else rtlIcon

@Composable
fun mirroringCancelIcon() = mirroringIcon(
    ltrIcon = Icons.Default.Close, rtlIcon = Icons.Outlined.ArrowForward
)


@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun NationalIdFiled(
    query: String ,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    enableClose:Boolean,
    modifier: Modifier = Modifier
) {
    MdcTheme() {
        Box(
            modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(25.dp),
                )) {
            if (query.isEmpty()) {
                Column( modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "لطفا شماره کارت ملی خود را وارد کنید",
                        color = Color.Gray
                    )
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
            ) {
                AnimatedVisibility(visible = enableClose) {
                    IconButton(onClick = onClearQuery) {
                        Icon(
                            imageVector = mirroringCancelIcon(),
                            tint = Color.Black,
                            contentDescription = "stringResource(R.string.label_back)"
                        )
                    }
                }

                val keyboardController = LocalSoftwareKeyboardController.current
                BasicTextField(
                    value = query,
                    singleLine = true,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding( 10.dp)
                        .onFocusChanged {
                            onSearchFocusChange(it.isFocused)
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        },
                    ),
                )

            }
        }
    }
}


fun ComposeView.viewToCompose(initialUi: @Composable () -> Unit) {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        initialUi()
    }
}