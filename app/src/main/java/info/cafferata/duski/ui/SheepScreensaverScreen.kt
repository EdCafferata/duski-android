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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

/**
 * Speelse screensaver: een schaap dat van rechts naar links loopt en over een
 * los hekje springt — het klassieke "schaapjes tellen"-beeld, als leuk
 * extraatje zodra er een geluid speelt. Tik ergens om terug te gaan.
 *
 * Richting: het 🐑-emoji kijkt van nature naar links, dus het schaap loopt
 * naar links zodat het lopen niet "achteruit" oogt.
 */
@Composable
fun SheepScreensaverScreen(onSluiten: () -> Unit) {
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

    val snelheid = 24.0
    val faseVerschuiving = 0.0

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1F3D), Color(0xFF0A0D1A))))
            .clickable { onSluiten() },
    ) {
        val density = LocalDensity.current
        val breedtePx = with(density) { maxWidth.toPx() }.toDouble()
        val hoogtePx = with(density) { maxHeight.toPx() }.toDouble()
        val grondLijnY = hoogtePx - 480.0
        val hekX = breedtePx * 0.5

        Text(
            "🌙",
            fontSize = 54.sp,
            modifier = Modifier.layout { m, c ->
                val p = m.measure(c)
                layout(p.width, p.height) {
                    p.place((breedtePx * 0.82 - p.width / 2).toInt(), (hoogtePx * 0.16 - p.height / 2).toInt())
                }
            },
        )

        Hekje(x = hekX.toFloat(), y = (grondLijnY - 20.0).toFloat())

        val breedteMetSchaap = breedtePx + 80.0
        val voortgang = (tijd * snelheid + faseVerschuiving * 220).mod(breedteMetSchaap)
        val x = breedteMetSchaap - voortgang - 40.0

        val wandelBob = abs(sin(tijd * snelheid / 30 + faseVerschuiving)) * 6
        val afstandTotHek = abs(x - hekX)
        val hekSpringBreedte = 90.0
        val sprong = if (afstandTotHek < hekSpringBreedte) {
            sin(PI * (1 - afstandTotHek / hekSpringBreedte)) * 50
        } else 0.0

        Text(
            "🐑",
            fontSize = 44.sp,
            modifier = Modifier.layout { m, c ->
                val p = m.measure(c)
                layout(p.width, p.height) {
                    p.place(
                        (x - p.width / 2).toInt(),
                        (grondLijnY - wandelBob - sprong - p.height / 2).toInt(),
                    )
                }
            },
        )

        Text(
            "Tik om terug te gaan",
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp),
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 13.sp,
        )
    }
}

/** Los hekje: twee ronde paaltjes met twee dwarsbalken ertussen. */
@Composable
private fun Hekje(x: Float, y: Float) {
    val breedte = 100f
    val hoogte = 64f
    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .layout { m, c ->
                val p = m.measure(androidx.compose.ui.unit.Constraints.fixed(breedte.toInt(), hoogte.toInt()))
                layout(p.width, p.height) {
                    p.place((x - breedte / 2).toInt(), (y - hoogte / 2).toInt())
                }
            },
    ) {
        val paalBreedte = 8f
        val paalX = listOf(breedte * 0.12f, breedte * 0.5f, breedte * 0.88f)
        for (px in paalX) {
            drawRoundRect(
                color = Color.White.copy(alpha = 0.4f),
                topLeft = androidx.compose.ui.geometry.Offset(px - paalBreedte / 2, 0f),
                size = androidx.compose.ui.geometry.Size(paalBreedte, hoogte),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(paalBreedte / 2, paalBreedte / 2),
            )
        }
        val balkHoogte = 9f
        for (balkY in listOf(hoogte * 0.22f, hoogte * 0.62f)) {
            drawRoundRect(
                color = Color.White.copy(alpha = 0.4f),
                topLeft = androidx.compose.ui.geometry.Offset(0f, balkY - balkHoogte / 2),
                size = androidx.compose.ui.geometry.Size(breedte, balkHoogte),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(balkHoogte / 2, balkHoogte / 2),
            )
        }
    }
}
