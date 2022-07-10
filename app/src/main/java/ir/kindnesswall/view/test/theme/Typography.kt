package ir.kindnesswall.view.test.theme


import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ir.kindnesswall.R

private val Caveat = FontFamily(
    Font(R.font.vazir, FontWeight.Normal)
)

private val BarlowCondensed = FontFamily(
    Font(R.font.vazir, FontWeight.Light),
    Font(R.font.vazir, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = BarlowCondensed,
        fontWeight = FontWeight.Light,
        fontSize = 30.sp,
        textAlign = TextAlign.Center
    ),
    h2 = TextStyle(
        fontFamily = Caveat,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    ),
    body2 = TextStyle(
        fontFamily = BarlowCondensed,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color(0xFF8b8da1),
        textAlign = TextAlign.Left
    )
)