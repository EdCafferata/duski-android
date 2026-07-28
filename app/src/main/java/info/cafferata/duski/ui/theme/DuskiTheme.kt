package info.cafferata.duski.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object Duski {
    val indigo = Color(0xFF6366F1)
    val nachtAchtergrond = Color(0xFF0F0F1A)
    val kaartAchtergrond = Color(0xFF1C1C2E)
}

@Composable
fun DuskiTheme(content: @Composable () -> Unit) {
    val scheme = darkColorScheme(
        primary = Duski.indigo,
        secondary = Duski.indigo,
        background = Duski.nachtAchtergrond,
        surface = Duski.kaartAchtergrond,
    )
    MaterialTheme(colorScheme = scheme, content = content)
}
