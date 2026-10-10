package info.cafferata.duski.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.cafferata.duski.R
import info.cafferata.duski.billing.Aanbieding

/**
 * Eén abonnementskaart op de paywall: naam, looptijd, prijs, koopknop en de
 * verlengingsvoorwaarden. De gratis proefperiode wordt alleen genoemd als de
 * gebruiker er nog recht op heeft.
 */
@Composable
fun PremiumProductKaart(aanbieding: Aanbieding, onKoop: () -> Unit) {
    val jaar = aanbieding.isJaarabonnement
    val gedempt = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(aanbieding.product.name, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            if (jaar) {
                Text(
                    stringResource(R.string.voordeligst),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            stringResource(if (jaar) R.string.plan_jaar else R.string.plan_maand, aanbieding.prijs),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(10.dp))
        Button(onClick = onKoop, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(if (aanbieding.heeftGratisProef) R.string.start_proef else R.string.abonneren))
        }
        Spacer(Modifier.height(8.dp))
        if (aanbieding.heeftGratisProef) {
            Text(stringResource(R.string.proef_regel), fontSize = 11.sp, color = gedempt, textAlign = TextAlign.Center)
        }
        Text(
            stringResource(if (jaar) R.string.verlenging_jaar else R.string.verlenging_maand, aanbieding.prijs),
            fontSize = 11.sp,
            color = gedempt,
            textAlign = TextAlign.Center,
        )
    }
}
