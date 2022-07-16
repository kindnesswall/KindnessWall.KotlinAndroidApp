package ir.kindnesswall.view.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kindnesswall.R

@Composable
fun BookedScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimary))
            .wrapContentSize(Alignment.Center)
    ) {
        AllWaitingGifts(fakeGifts)
    }
}

@Preview(showBackground = true)
@Composable
fun BookedScreenPreview() {
    BookedScreen()
}

@Composable
fun ReceivedScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimary))
            .wrapContentSize(Alignment.Center)
    ) {
        AllWaitingGifts(fakeGifts)
    }
}

@Preview(showBackground = true)
@Composable
fun ReceivedScreenPreview() {
    ReceivedScreen()
}

@Composable
fun AllWaitingGifts(gifts: List<SimpleGiftPresentation>) {
    Scaffold(
    ) {
        LazyColumn(
            Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(gifts) { gift ->
                GiftCard(gift.name, gift.description, gift.imageRes)
            }
        }
    }
}