package info.cafferata.duski.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.cafferata.duski.model.ScreensaverBeweging
import info.cafferata.duski.model.ScreensaverConfig
import kotlin.math.floor
import kotlin.math.sin

/**
 * Herbruikbare screensaver-animatie: plaatst een aantal drijvende, vallende,
 * glijdende, twinkelende of deinende emoji-elementen over een verticaal
 * kleurverloop — puur tijd-gedreven animatie, geen video- of mediabestanden
 * van derden nodig (zelfde rechtenvrije aanpak als de audio-generators). Tik
 * ergens om terug te gaan.
 */
@Composable
fun AnimatedScreensaverScreen(config: ScreensaverConfig, onSluiten: () -> Unit) {
    var tijd by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        var start = -1L
        while (true) {
            withFrameNanos { now ->
                if (start < 0) start = now
                tijd = (now - start) / 1_000_000_000f
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(config.achtergrondBoven, config.achtergrondOnder)))
            .clickable { onSluiten() },
    ) {
        val density = LocalDensity.current
        val breedtePx = with(density) { maxWidth.toPx() }
        val hoogtePx = with(density) { maxHeight.toPx() }

        for (index in 0 until config.aantalElementen) {
            ScreensaverElement(index, tijd, breedtePx, hoogtePx, config)
        }

        Text(
            "Tik om terug te gaan",
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp),
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 13.sp,
        )
    }
}

private data class ElementPositie(
    val x: Float,
    val y: Float,
    val fontSizeSp: Float,
    val scale: Float,
    val opacity: Float,
)

@Composable
private fun ScreensaverElement(index: Int, tijd: Float, breedtePx: Float, hoogtePx: Float, config: ScreensaverConfig) {
    val seed = index.toDouble()
    val faseVerschuiving = fractie(seed * 78.233)
    val snelheid = config.snelheid * (0.7 + 0.6 * fractie(seed * 37.719))

    val positie = when (config.beweging) {
        ScreensaverBeweging.GLIJDEN_ZIJWAARTS -> {
            val baanY = hoogtePx * (0.15 + 0.7 * fractie(seed * 12.9898))
            val breedteMetElement = breedtePx + 80
            val voortgang = (tijd * snelheid + faseVerschuiving * breedteMetElement).mod(breedteMetElement)
            val x = breedteMetElement - voortgang - 40
            ElementPositie(x.toFloat(), baanY.toFloat(), 34f, 1f, 1f)
        }
        ScreensaverBeweging.VALLEN_OMLAAG -> {
            val hoogteMetElement = hoogtePx + 80
            val voortgang = (tijd * snelheid * 20 + faseVerschuiving * hoogteMetElement).mod(hoogteMetElement)
            val x = breedtePx * fractie(seed * 12.9898)
            ElementPositie(x.toFloat(), (voortgang - 40).toFloat(), 20f, 1f, 1f)
        }
        ScreensaverBeweging.DRIJVEN_OMHOOG -> {
            val hoogteMetElement = hoogtePx + 80
            val voortgang = (tijd * snelheid * 14 + faseVerschuiving * hoogteMetElement).mod(hoogteMetElement)
            val x = breedtePx * fractie(seed * 12.9898)
            val deining = sin(tijd * 0.6 + seed * 6) * 14
            ElementPositie((x + deining).toFloat(), (hoogteMetElement - voortgang - 40).toFloat(), 22f, 1f, 1f)
        }
        ScreensaverBeweging.TWINKELEN -> {
            val x = breedtePx * (if (index == 0) 0.5 else fractie(seed * 12.9898))
            val y = hoogtePx * (if (index == 0) 0.42 else (0.1 + 0.5 * fractie(seed * 45.164)))
            val puls = 0.6 + 0.4 * sin(tijd * (1.2 + fractie(seed * 91.7)) + seed * 6)
            val grootte = if (index == 0) 64f else 26f
            ElementPositie(x.toFloat(), y.toFloat(), grootte, (0.8 + 0.3 * puls).toFloat(), (0.4 + 0.6 * puls).toFloat())
        }
        ScreensaverBeweging.DEINEN -> {
            val x = breedtePx * (if (index == 0) 0.5 else (0.1 + 0.8 * fractie(seed * 12.9898)))
            val baseY = hoogtePx * (if (index == 0) 0.42 else (0.3 + 0.5 * fractie(seed * 45.164)))
            val deining = sin(tijd * 0.8 + seed * 5) * 18
            val grootte = if (index == 0) 64f else 30f
            ElementPositie(x.toFloat(), (baseY + deining).toFloat(), grootte, 1f, 1f)
        }
    }

    Text(
        config.elementEmoji,
        fontSize = positie.fontSizeSp.sp,
        color = Color.White,
        modifier = Modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(
                        (positie.x - placeable.width / 2).toInt(),
                        (positie.y - placeable.height / 2).toInt(),
                    )
                }
            }
            .scale(positie.scale)
            .alpha(positie.opacity),
    )
}

/**
 * Deterministische pseudo-willekeurige fractie tussen 0 en 1, puur op basis
 * van een seed-waarde (geen echte randomness nodig voor een stabiele lay-out
 * per element).
 */
private fun fractie(waarde: Double): Double {
    val x = sin(waarde) * 43758.5453
    return x - floor(x)
}
