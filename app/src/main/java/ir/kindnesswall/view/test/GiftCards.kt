package ir.kindnesswall.view.test

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import ir.kindnesswall.view.main.charity.charitydetail.CharityDetailActivity
import ir.kindnesswall.view.test.theme.GiftPresentationCosmeticsTheme
import timber.log.Timber

data class SimpleGiftPresentation(
    val id: Int,
    val name: String,
    val description: String,
    val imageRes: Int
)

@Composable
fun GiftCard(name: String, description: String, image: Int) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                val gift = GiftModel()
                GiftDetailActivity.start(context, gift)
            }
        ) {
            Column(
                Modifier.padding(8.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .background(Color.Cyan),
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .background(Color.Green)
                )
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp)
                    .border(BorderStroke(width = 2.dp, color = Color.Black)),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Composable
fun GiftCardPreview() {
    GiftPresentationCosmeticsTheme {
        GiftCard(fakeGifts[0].name, fakeGifts[0].description, fakeGifts[0].imageRes)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Composable
fun GiftCard1Preview() {
    GiftPresentationCosmeticsTheme {
        GiftCard(fakeGifts[1].name, fakeGifts[1].description, fakeGifts[1].imageRes)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Composable
fun GiftCard2Preview() {
    GiftPresentationCosmeticsTheme {
        GiftCard(fakeGifts[2].name, fakeGifts[2].description, fakeGifts[2].imageRes)
    }
}

val fakeGifts = listOf(
    SimpleGiftPresentation(
        1,
        "",
        "",
        R.drawable.chats_place_holder,
    ),
    SimpleGiftPresentation(
        1,
        "عنوان",
        "عنوان دوم",
        R.drawable.chats_place_holder,
    ),
    SimpleGiftPresentation(
        1,
        "هدیه",
        "",
        R.drawable.chats_place_holder,
    )
)