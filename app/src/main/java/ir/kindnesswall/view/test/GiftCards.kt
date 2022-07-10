package ir.kindnesswall.view.test

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kindnesswall.view.test.theme.PlantsInCosmeticsTheme
import ir.kindnesswall.R
import ir.kindnesswall.utils.extentions.setBackground

data class Plant(
    val id: Int,
    val name: String,
    val description: String,
    val imageRes: Int
)

@Composable
fun PlantCard(name: String, description: String, image: Int) {
    Card(
        modifier = Modifier
            // The space between each card and the other
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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
                    modifier = Modifier.padding(bottom = 4.dp)
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
fun PlanetCardPreview() {
    PlantsInCosmeticsTheme {
        PlantCard(plants[0].name, plants[0].description, plants[0].imageRes)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Composable
fun PlanetCard1Preview() {
    PlantsInCosmeticsTheme {
        PlantCard(plants[1].name, plants[1].description, plants[1].imageRes)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Composable
fun PlanetCard2Preview() {
    PlantsInCosmeticsTheme {
        PlantCard(plants[2].name, plants[2].description, plants[2].imageRes)
    }
}

val plants = listOf(
    Plant(
        1,
        "",
        "",
        R.drawable.chats_place_holder,
    ),
    Plant(
        1,
        "عنوان",
        "عنوان دوم",
        R.drawable.chats_place_holder,
    ),
    Plant(
        1,
        "هدیه",
        "",
        R.drawable.chats_place_holder,
    )
)