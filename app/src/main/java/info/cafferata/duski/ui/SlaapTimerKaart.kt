package info.cafferata.duski.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.cafferata.duski.audio.GeluidsMixer
import info.cafferata.duski.audio.SlaapTimer

/** Kaart onderaan het mixer-scherm waarmee je een sleeptimer instelt. */
@Composable
fun SlaapTimerKaart(
    timer: SlaapTimer,
    mixer: GeluidsMixer,
    actieveOptieId: String?,
    toontScreensaverKnop: Boolean,
    onScreensaver: () -> Unit,
) {
    val opties = listOf(15, 30, 45, 60, 90)
    val resterend = timer.resterendeSeconden.value

    Column {
        Text("Sleeptimer", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer12()

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            if (toontScreensaverKnop) {
                ScreensaverKnop(onScreensaver)
            }

            if (resterend != null) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(tijdText(resterend), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Button(
                        onClick = { timer.stop() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    ) {
                        Text("Stop")
                    }
                }
            } else {
                opties.forEach { minuten ->
                    Column(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF3A3A4A), CircleShape)
                            .clickable {
                                actieveOptieId?.let { timer.start(minuten, mixer, it) }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text("$minuten", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("min", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreensaverKnop(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(60.dp)
            .background(Color(0xFF6366F1).copy(alpha = 0.18f), CircleShape)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("🌙", fontSize = 22.sp)
    }
}

@Composable
private fun Spacer12() {
    androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 6.dp))
}

private fun tijdText(seconden: Int): String {
    val m = seconden / 60
    val s = seconden % 60
    return "%02d:%02d".format(m, s)
}
