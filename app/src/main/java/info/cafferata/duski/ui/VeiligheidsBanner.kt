package info.cafferata.duski.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Onderzoek toont dat alle geteste witte-ruismachines voor baby's op vol volume
 * de door ziekenhuizen aanbevolen geluidsniveaus overschreden. Deze banner maakt
 * de ingebouwde volumelimiet zichtbaar in plaats van een verborgen instelling.
 * Verdwijnt na een minuut vanzelf zodat de lange tekst niet blijvend de
 * uitlijning van de rest van het scherm verstoort.
 */
@Composable
fun VeiligheidsBanner(onVerlopen: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(60_000)
        onVerlopen()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF9800).copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF9800))
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                "Veilig volume voor je baby",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                "Het volume is begrensd en het toestel hoort minstens 2 meter van de baby vandaan te staan, volgens AAP-richtlijnen.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}
