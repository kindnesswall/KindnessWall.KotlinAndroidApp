package ir.kindnesswall.view.test.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


val teal700 = Color(0xFF00796b)
val tealDark = Color(0xFF004c40)
val red100 = Color(0xFFff8a80)
val redDark = Color(0xFFc85a54)

val Shapes = Shapes(
    small = RoundedCornerShape(topEnd = 25.dp),
    medium = RoundedCornerShape(4.dp),
    large = CutCornerShape(topStart = 50.dp)
)

private val DarkColorPalette = darkColors(
    primary = teal700,
    primaryVariant = tealDark,
    secondary = redDark,
    background = Color.DarkGray,
    surface = Color.DarkGray,
    onPrimary = Color.White,
)

private val LightColorPalette = lightColors(
    primary = teal700,
    primaryVariant = tealDark,
    secondary = red100,
    onPrimary = Color.White,
)

@Composable
fun PlantsInCosmeticsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}